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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
}
