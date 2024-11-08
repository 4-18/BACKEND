package com.blueDragon.Convenience.Dto.Product;

import com.blueDragon.Convenience.Model.ProductComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCommentDto {
    private Long id;              // 댓글 ID
    private String comment;       // 댓글 내용
    private LocalDateTime createdAt; // 생성 시간

    // ProductComment 엔티티를 CommentDto로 변환
    public static ProductCommentDto entityToDto(ProductComment productComment) {
        return ProductCommentDto.builder()
                .id(productComment.getId())
                .comment(productComment.getComment())
                .createdAt(productComment.getCreatedAt()) // BaseEntity의 createdAt
                .build();
    }
}
