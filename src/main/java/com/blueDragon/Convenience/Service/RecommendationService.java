package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Recommendation.RequestRecommendationDto;
import com.blueDragon.Convenience.Dto.Recommendation.ResponseRecommendationDto;
import com.blueDragon.Convenience.Exception.RecommendationEmptyException;
import com.blueDragon.Convenience.Exception.RecommendationNotExistException;
import com.blueDragon.Convenience.Exception.UserNotExistException;
import com.blueDragon.Convenience.Model.*;
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
    private final RecommendationRepository recommendationRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ResponseRecommendationDto register(RequestRecommendationDto recommendationDto, List<MultipartFile> files, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("존재하지 않는 유저입니다."));


        List<Product> productList = productService.getProductFromDto(recommendationDto.getProductList());
        Integer totalPrice = productService.sumProductPrices(productList);
        List<FoodType> foodTypes = productService.combineFoodTypes(productList);
        List<ConvenienceType> availableAt = productService.combineConvenienceTypes(productList);
        List<String> urls = files.stream().map((multipartFile -> {
            try {
                return s3Uploader.upload(multipartFile, "recommend");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })).toList();

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
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public List<ResponseRecommendationDto> getLikedRecommendationByUser(String username) {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findLikedRecommendationByUser(username);
        if (recommendBoardList.isEmpty()) {
            throw new RecommendationEmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToDto)).collect(Collectors.toList());
    }

    public ResponseRecommendationDto getRecommendationById(Long id) {
        RecommendBoard recommendBoard = recommendationRepository.findById(id)
                .orElseThrow(() -> new RecommendationNotExistException("존재하지 않는 레시피입니다."));

        System.out.println(recommendBoard.getProductList());
        return ResponseRecommendationDto.entityToCreateDto(recommendBoard);
    }
}
