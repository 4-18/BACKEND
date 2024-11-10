package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.RecommendationLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationLikeController {
    private final RecommendationLikeService recommendationLikeService;

    @PostMapping("/{recommendation_id}/like")
    public ResponseEntity<ResponseDTO<?>> RecommendationLikeRegister(@AuthenticationPrincipal User user, @PathVariable("recommendation_id") Long id) {
        System.out.println(user.getUsername());
        recommendationLikeService.register(user.getUsername(), id);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_REACTION_REGISTER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REACTION_REGISTER, null));
    }
}
