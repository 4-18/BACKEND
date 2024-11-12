package com.blueDragon.Convenience.Code;

// 프론트에서 받는 응답을 일정하게 유지해주기 위해
// 간단히 우리만의 알아들을 수 있는 설명과 에러 코드를 전달합니다.
// 우리 프로젝트에 맞는 오류 메시지를 전달하게 됨.

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.BAD_REQUEST, "중복된 아이디를 사용할 수 없습니다."),


    /**
     * 401 UNAUTHORIZED: 토큰 만료
     */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh 토큰입니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "요청 헤더에 토큰이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),

    /**
     * 403
     */
    HAVE_NO_PERMISSION(HttpStatus.UNAUTHORIZED, "작성자가 아닙니다."),

    /**
     * 404
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."),
    HAVE_NO_AUTHORITY(HttpStatus.NOT_FOUND, "권한이 없습니다."),
    RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 레시피가 존재하지 않습니다."),

    INVALID_VALUE(HttpStatus.NOT_ACCEPTABLE, "잘못된 값입니다."),
    INVALID_CATEGORY_VALUE(HttpStatus.NOT_ACCEPTABLE, "잘못된 카테고리 값입니다."),


            ;

    private final HttpStatus status;
    private final String message;
}
