package com.blueDragon.Convenience.Dto.Product;

import com.blueDragon.Convenience.Model.RecommendationComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationCommentDto {
    private String comment;       // 댓글 내용

    // RecommendationComment 엔티티를 RecommendationCommentDto로 변환
    public static RecommendationCommentDto entityToDto(RecommendationComment recommendationComment) {
        return RecommendationCommentDto.builder()
                .comment(recommendationComment.getComment())
                .build();
    }
}
