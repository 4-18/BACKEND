package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Dto.Comment.ProductCommentDto;
import com.blueDragon.Convenience.Dto.Comment.ResponseProductCommentDto;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.ProductCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "상품 댓글 API")
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductCommentController {

    private final ProductCommentService productCommentService;

    // 상품 댓글 작성
    @PostMapping("/{id}/comments")
    public ResponseEntity<ProductCommentDto> addComment(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody ProductCommentDto commentDto) {
        return ResponseEntity.ok(productCommentService.addComment(user.getUsername(), id, commentDto));
    }

    @GetMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseProductCommentDto> getComments(@PathVariable Long comment_id) {
        return ResponseEntity.ok(productCommentService.getComment(comment_id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<ResponseProductCommentDto>> getCommentsByProductId(@PathVariable Long id) {
        List<ResponseProductCommentDto> productCommentDtos = productCommentService.getCommentsByProductId(id);
        return ResponseEntity.ok(productCommentDtos);
    }

    // 상품 댓글 수정
//    @PutMapping("/comments/{comment_id}")
//    public ResponseEntity<ResponseProductCommentDto> updateComment(@PathVariable Long comment_id, @RequestBody ProductCommentDto commentDto) {
//        return ResponseEntity.ok(productCommentService.updateComment(comment_id, commentDto));
//    }

    // 상품 댓글 삭제
//    @DeleteMapping("/comments/{comment_id}")
//    public ResponseEntity<ProductCommentDto> deleteComment(@PathVariable Long comment_id) {
//        productCommentService.deleteComment(comment_id);
//        return ResponseEntity.noContent().build();
//    }
}
