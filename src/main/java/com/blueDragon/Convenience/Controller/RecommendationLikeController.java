package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.RecommendationLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "레시피 평가 API")
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationLikeController {
    private final RecommendationLikeService recommendationLikeService;

    @PostMapping("/{recommendation_id}/like")
    @Operation(summary = "레시피 좋아요 평가", description = "레시피에 좋아요로 평가를 합니다.")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{ \"status\": 200, \"code\": \"SUCCESS_REACTION_REGISTER\", \"message\": \"성공적으로 평가를 처리되었습니다.\", \"data\": null }")))
    @ApiResponse(responseCode = "404", description = "해당 상품이 존재하지 않을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{ \"status\": 404, \"error\": \"NOT_FOUND\", \"code\": \"PRODUCT_NOT_FOUND\", \"message\": \"해당 상품이 존재하지 않습니다.\" }")))
    public ResponseEntity<ResponseDTO<?>> RecommendationLikeRegister(@AuthenticationPrincipal User user, @PathVariable("recommendation_id") Long id) {
        System.out.println(user.getUsername());
        recommendationLikeService.register(user.getUsername(), id);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_REACTION_REGISTER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REACTION_REGISTER, null));
    }
}
