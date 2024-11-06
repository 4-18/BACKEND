package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Code.ResponseCode;
import com.blueDragon.Convenience.Dto.Response.ResponseDTO;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.ProductLikeOrHateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductLikeOrHateController {
    private final ProductLikeOrHateService productLikeOrHateService;

    @PostMapping("/{product_id}/{reaction}")
    public ResponseEntity<ResponseDTO<?>> ProductLikeORHateRegister(@AuthenticationPrincipal User user, @PathVariable("product_id") Long id, @PathVariable("reaction") String reaction) {
        System.out.println(user.getUsername());
        productLikeOrHateService.register(user.getUsername(), id, reaction);
        return ResponseEntity
                .status(ResponseCode.SUCCESS_REACTION_REGISTER.getStatus().value())
                .body(new ResponseDTO<>(ResponseCode.SUCCESS_REACTION_REGISTER, null));
    }


}
