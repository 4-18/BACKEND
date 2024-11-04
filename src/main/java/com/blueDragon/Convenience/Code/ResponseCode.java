package com.blueDragon.Convenience.Code;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 프론트에서 받는 응답을 일정하게 유지해주기 위해
// 간단히 우리만의 알아들을 수 있는 설명과 성공 코드를 전달합니다.
// 우리 프로젝트에 맞는 성공 메시지를 전달하게 됨.

@RequiredArgsConstructor
@Getter

public enum ResponseCode {
    /**
     * User
     */
    SUCCESS_RETRIEVE_USER(HttpStatus.OK, "유저 조회를 성공했습니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다."),
    SUCCESS_RETRIEVE_USERINFO(HttpStatus.OK,"유저 정보를 성공적으로 가져왔습니다."),


    ;

    private final HttpStatus status;
    private final String message;

}
