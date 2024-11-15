package com.blueDragon.Convenience.Dto.Product;

import com.blueDragon.Convenience.Model.ProductComment;
import com.blueDragon.Convenience.Model.RecommendationComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class ResponseRecommendationCommentDto {
    private String comment;
    private String nickname;
    private LocalDateTime createdAt;

    public static ResponseRecommendationCommentDto entityToDto(RecommendationComment recommendationComment) {
        return ResponseRecommendationCommentDto.builder()
                .comment(recommendationComment.getComment())
                .nickname(recommendationComment.getUser().getNickname())
                .createdAt(recommendationComment.getCreatedAt())
                .build();
    }
}
