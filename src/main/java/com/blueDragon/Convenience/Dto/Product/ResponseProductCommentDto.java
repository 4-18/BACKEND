package com.blueDragon.Convenience.Dto.Product;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseProductCommentDto {
    private String comment;
    private String nickname;
    private LocalDateTime createdAt;
}
