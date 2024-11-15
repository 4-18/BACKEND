package com.blueDragon.Convenience.Dto.Comment;

import com.blueDragon.Convenience.Model.ProductComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseProductCommentDto {
    private String comment;
    private String nickname;
    private LocalDateTime createdAt;

    public static ResponseProductCommentDto entityToDto(ProductComment productComment) {
        return ResponseProductCommentDto.builder()
                .comment(productComment.getComment())
                .nickname(productComment.getUser().getNickname())
                .createdAt(productComment.getCreatedAt())
                .build();
    }
}
