package com.blueDragon.Convenience.Service;

import com.blueDragon.Convenience.Dto.Recommendation.RequestRecommendationDto;
import com.blueDragon.Convenience.Dto.Recommendation.ResponseRecommendationDto;
import com.blueDragon.Convenience.Exception.EmptyException;
import com.blueDragon.Convenience.Exception.UserNotExistException;
import com.blueDragon.Convenience.Model.*;
import com.blueDragon.Convenience.Repository.RecommendationRepository;
import com.blueDragon.Convenience.Repository.UserRepository;
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
                                .imageUrl(urls.toString())
                                .recommendLikes(new ArrayList<>())
                                .productList(productList)
                                .foodTypes(foodTypes)
                                .availableAt(availableAt)
                                .totalPrice(totalPrice)
                                .user(user)
                                .build();

        recommendationRepository.save(entity);
        return ResponseRecommendationDto.entityToCreateDto(entity);


    }

    public List<ResponseRecommendationDto> getRecommendationByNewest() {
        List<RecommendBoard> recommendBoardList = recommendationRepository.findAllRecommendationOrderByIdDesc();
        if (recommendBoardList.isEmpty()) {
            // 레시피 글이 비어있다는 예외처리가 에러코드에 아직 구현이 안 됐으므로 임시로 상품 예외처리 코드 연결
            // 추후에 바꿔야함
            throw new EmptyException("비어있습니다.");
        }
        return recommendBoardList.stream().map((ResponseRecommendationDto::entityToCreateDto)).collect(Collectors.toList());
    }
}
