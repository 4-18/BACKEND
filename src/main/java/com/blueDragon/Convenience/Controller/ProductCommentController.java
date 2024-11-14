package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Dto.Product.ProductCommentDto;
import com.blueDragon.Convenience.Service.ProductCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductCommentDto> addComment(@PathVariable Long id, @RequestBody ProductCommentDto commentDto) {
        return ResponseEntity.ok(productCommentService.addComment(id, commentDto));
    }

    @GetMapping("/comments/{comment_id}")
    public ResponseEntity<ProductCommentDto> getComments(@PathVariable Long comment_id) {
        return ResponseEntity.ok(productCommentService.getComment(comment_id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<ProductCommentDto>> getCommentsByProductId(@PathVariable Long id) {
        List<ProductCommentDto> productCommentDtos = productCommentService.getCommentsByProductId(id);
        return ResponseEntity.ok(productCommentDtos);
    }

    // 상품 댓글 수정
    @PutMapping("/comments/{comment_id}")
    public ResponseEntity<ProductCommentDto> updateComment(@PathVariable Long comment_id, @RequestBody ProductCommentDto commentDto) {
        return ResponseEntity.ok(productCommentService.updateComment(comment_id, commentDto));
    }

    // 상품 댓글 삭제
    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<ProductCommentDto> deleteComment(@PathVariable Long comment_id) {
        productCommentService.deleteComment(comment_id);
        return ResponseEntity.noContent().build();
    }
}
