package com.blueDragon.Convenience.Controller;


import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Recommendation.RequestRecommendationDto;
import com.blueDragon.Convenience.Dto.Recommendation.ResponseRecommendationDto;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

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

    @GetMapping("/liked")
    public ResponseEntity<?> getLikedRecommendationByUser(@Valid @AuthenticationPrincipal User user) {
        List<ResponseRecommendationDto> list = recommendationService.getLikedRecommendationByUser(user.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/popular")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByPopular() {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByPopular();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/newest")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByNewest() {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByNewest();
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseDTO<?>> getRecommendationByUser(@Valid @AuthenticationPrincipal User user) {
        List<ResponseRecommendationDto> list = recommendationService.getRecommendationByUser(user.getUsername());
        return ResponseEntity
                .status(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_RETRIEVE_RECOMMENDATION_LIST, list));
    }
}
