package com.blueDragon.Convenience.Controller;


import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Product.ProductDto;
import com.blueDragon.Convenience.Dto.Recommendation.RequestRecommendationDto;
import com.blueDragon.Convenience.Dto.Recommendation.ResponseRecommendationDto;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "레시피 API")
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @Operation(summary = "레시피 작성", description = "레시피 추천글을 작성합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> RecommendationRegister(@Valid @RequestPart("createRecommendationDto")RequestRecommendationDto recommendationDto,
                                                    @RequestPart(value = "files", required = false) List<MultipartFile> files, @AuthenticationPrincipal User user) {

        ResponseRecommendationDto res = recommendationService.register(recommendationDto, files, user.getUsername());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_REGISTER_RECOMMENDATIONS.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REGISTER_RECOMMENDATIONS, res));
    }


    @GetMapping("/{recommendation_id}")
    public ResponseEntity<?> getRecommendationById(@PathVariable("recommendation_id") Long id) {
        ResponseRecommendationDto res = recommendationService.getRecommendationById(id);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION, res));
    }

    @GetMapping("/price/{price}")
    @Operation(summary = "레시피 리스트 - 가격순", description = "레시피 리스트를 가격순으로 필터링합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByPrice(
            @Parameter(required = true, description = "기준이 되는 금액")
            @PathVariable int price) {
        List<ResponseRecommendationDto> list = recommendationService.getProductByPrice(price);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByCategory(@PathVariable String category) {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByCategory(category);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/convenience/{convenience}")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByConvenience(@PathVariable("convenience") String name) {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByConvenience(name);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/liked")
    @Operation(summary = "내가 좋아요 누른 레시피 불러오기", description = "내가 좋아요 누른 레시피를 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<?> getLikedRecommendationByUser(@Valid @AuthenticationPrincipal User user) {
        List<ResponseRecommendationDto> list = recommendationService.getLikedRecommendationByUser(user.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/popular")
    @Operation(summary = "레시피 리스트 - 추천순", description = "레시피 좋아요가 가장 많은 순서대로 레시피를 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByPopular() {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByPopular();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/comment")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByCommentPopular() {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByCommentPopular();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/newest")
    @Operation(summary = "레시피 리스트 - 최신순", description = "레시피를 최신순으로 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByNewest() {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByNewest();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 쓴 글 불러오기", description = "내가 작성한 레시피를 불러옵니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByUser(@Valid @AuthenticationPrincipal User user) {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByUser(user.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }
}
