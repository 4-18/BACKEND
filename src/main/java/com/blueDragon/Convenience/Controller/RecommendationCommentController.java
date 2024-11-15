package com.blueDragon.Convenience.Controller;

import com.blueDragon.Convenience.Dto.Product.RecommendationCommentDto;
import com.blueDragon.Convenience.Dto.Product.ResponseRecommendationCommentDto;
import com.blueDragon.Convenience.Model.User;
import com.blueDragon.Convenience.Service.RecommendationCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "꿀조합 댓글 API")
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationCommentController {

    private final RecommendationCommentService recommendationCommentService;

    // 추천 댓글 작성
    @PostMapping("/{id}/comments")
    public ResponseEntity<RecommendationCommentDto> addComment(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody RecommendationCommentDto commentDto) {
        return ResponseEntity.ok(recommendationCommentService.addComment(user.getUsername(), id, commentDto));
    }

    @GetMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseRecommendationCommentDto> getComments(@PathVariable Long comment_id) {
        return ResponseEntity.ok(recommendationCommentService.getComment(comment_id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<ResponseRecommendationCommentDto>> getCommentsByRecommendationId(@PathVariable Long id) {
        List<ResponseRecommendationCommentDto> recommendationCommentDtos = recommendationCommentService.getCommentsByBoardId(id);
        return ResponseEntity.ok(recommendationCommentDtos);
    }

//    // 추천 댓글 수정
//    @PutMapping("/comments/{comment_id}")
//    public ResponseEntity<RecommendationCommentDto> updateComment(@PathVariable Long comment_id, @RequestBody RecommendationCommentDto commentDto) {
//        return ResponseEntity.ok(recommendationCommentService.updateComment(comment_id, commentDto));
//    }
//
//    // 추천 댓글 삭제
//    @DeleteMapping("/comments/{comment_id}")
//    public ResponseEntity<RecommendationCommentDto> deleteComment(@PathVariable Long comment_id) {
//        recommendationCommentService.deleteComment(comment_id);
//        return ResponseEntity.noContent().build();
//    }
}
