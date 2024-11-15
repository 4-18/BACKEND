package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Dto.Recommendation.RequestRecommendationDto;
import com.blueDragon.Convenience.Dto.Recommendation.ResponseRecommendationDto;
import com.blueDragon.Convenience.Exception.*;
import com.blueDragon.Convenience.Model.*;
import com.blueDragon.Convenience.Repository.ProductLikeRepository;
import com.blueDragon.Convenience.Repository.ProductRepository;
import com.blueDragon.Convenience.Repository.RecommendationRepository;
import com.blueDragon.Convenience.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class RecommendationService {
    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductLikeRepository productLikeRepository;
    private final RecommendationRepository recommendationRepository;
    private final ConvenienceService convenienceService;
    private final CategoryService categoryService;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseRecommendationDto register(RequestRecommendationDto recommendationDto, List<MultipartFile> files, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("존재하지 않는 유저입니다."));

        List<Product> productList = productService.getProductFromDto(recommendationDto.getProductList());
        Integer totalPrice = productService.sumProductPrices(productList);
        List<String> foodTypes = categoryService.combineFoodTypes(productList);
        List<String> availableAt = convenienceService.combineConvenienceTypes(productList);
        List<String> urls = files.isEmpty() ? List.of() : files.stream()
                .map(multipartFile -> {
                    try {
                        return s3Uploader.upload(multipartFile, "recommend");
                    } catch (IOException e) {
                        throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
                    }
                })
                .toList();


        RecommendBoard entity = RecommendBoard.builder()
                                .title(recommendationDto.getTitle())
                                .content(recommendationDto.getContent())
                                .imageUrl(String.join(",", urls)) // 쉼표로 구분된 문자열로 변환

                                .recommendLikes(new ArrayList<>())
                                .productList(productList)
                                .foodTypes(foodTypes)
                                .availableAt(availableAt)
                                .totalPrice(totalPrice)
                                .user(user)
                                .build();

        productList.forEach(product -> product.setRecommendBoard(entity));
        recommendationRepository.save(entity);
        return ResponseRecommendationDto.entityToCreateDto(entity);


    }

    public List<ResponseRecommendationDto> getRecommendationByPopular() {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findAllByOrderByLikeCountDesc();
        if (recommendBoardList.isEmpty()) {
            throw new RecommendationEmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public List<ResponseRecommendationDto> getRecommendationByNewest() {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findAllRecommendationOrderByIdDesc();
        if (recommendBoardList.isEmpty()) {
            throw new RecommendationEmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map(recommendBoard -> {
            ResponseRecommendationDto dto = ResponseRecommendationDto.entityToDto(recommendBoard);
            dto.setProductList(recommendBoard.getProductList().stream().map(product -> {
                ProductDto productDto = ProductDto.entityToDto(product);
                productDto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
                return productDto;
            }).collect(Collectors.toList()));
            return dto;  // 수정된 dto 객체를 반환해야 합니다.
        }).collect(Collectors.toList());
    }

    public List<ResponseRecommendationDto> getLikedRecommendationByUser(String username) {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findLikedRecommendationByUser(username);
        if (recommendBoardList.isEmpty()) {
            throw new RecommendationEmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public List<ResponseRecommendationDto> getRecommendationByUser(String username) {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findRecommendationByUser(username);
        if (recommendBoardList.isEmpty()) {
            throw new RecommendationEmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public List<ResponseRecommendationDto> getRecommendationByCommentPopular() {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findAllByOrderByCommentCountDesc();
        if (recommendBoardList.isEmpty()) {
            throw new RecommendationEmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public ResponseRecommendationDto getRecommendationById(Long id) {
        RecommendBoard recommendBoard = recommendationRepository.findById(id)
                .orElseThrow(() -> new RecommendationNotExistException("존재하지 않는 레시피입니다."));

        System.out.println(recommendBoard.getProductList());
        ResponseRecommendationDto dto = ResponseRecommendationDto.entityToDto(recommendBoard);
        dto.setProductList(recommendBoard.getProductList().stream().map(product -> {
            ProductDto productDto = ProductDto.entityToDto(product);
            productDto.setCountLikes(productLikeRepository.countLikesByProductId(product.getId()));
            return productDto;
        }).collect(Collectors.toList()));
        return dto;  // 수정된 dto 객체를 반환해야 합니다.
    }

    public List<ResponseRecommendationDto> getProductByPrice(int price) {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findRecommendBoardsByMaxTotalPrice(price);
        if (recommendBoardList.isEmpty()) {
            throw new EmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public List<ResponseRecommendationDto> getRecommendationByCategory(String category) {
        // Convert category string to FoodType enum
        if (categoryService.getProductByCategory(category)) {
            // Get products by food type and return mapped DTO list
            List<RecommendBoard> recommendBoardList = recommendationRepository.findByFoodType(category);
            if (recommendBoardList.isEmpty()) {
                throw new RecommendationEmptyException("요청은 됐는데 빔");
            }

            return recommendBoardList.stream().map(ResponseRecommendationDto::entityToDto).collect(Collectors.toList());
        } else {
            throw new CategoryInvalidValueException("카테고리가 잘못 선택되었습니다.");
        }
    }

    public List<ResponseRecommendationDto> getRecommendationByConvenience(String name) {
        if (convenienceService.getCovenience(name)) {
            List<RecommendBoard> recommendBoardList = recommendationRepository.findByAvailableAt(name);
            return recommendBoardList.stream().map(ResponseRecommendationDto::entityToDto).collect(Collectors.toList());
        } else {
            throw new ConvenienceInvalidValueException("편의점이 잘못 선택되었습니다.");
        }
    }

}
