package com.blueDragon.Convenience.Dto.Comment;

import com.blueDragon.Convenience.Model.ProductComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCommentDto {
    private String comment;       // 댓글 내용 >> 사용자가 댓글 작성할 때 필요한 값

    // ProductComment 엔티티를 CommentDto로 변환
    public static ProductCommentDto entityToDto(ProductComment productComment) {
        return ProductCommentDto.builder()
                .comment(productComment.getComment())
                .build();
    }
}
