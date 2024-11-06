package com.blueDragon.Convenience.Exception;


import com.blueDragon.Convenience.Code.ErrorCode;
import com.blueDragon.Convenience.Dto.Response.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice //API 리퀘스트로 넘어온 정보에 @Valid 라고만 적어주면 해당 요청이 맞게 들어왔는지
        // 비어있으면 안 ㅗ디는 정보가 비어져서 들어왔는지를 확인해줌
        // 그리고 그에 맞는 예외처리를 해주는데 일관된 응답으로 프론트에게 정확한 정보 전달
        // 컨트롤러 전역에서 발생하는 예외를 처리하게 됨.
@Slf4j

public class GlobalExceptionHandler {
    /**
     * 입력값 검증
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler(DuplicateLoginIdException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDuplicateLoginIdException(final DuplicateLoginIdException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATE_LOGIN_ID.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.DUPLICATE_LOGIN_ID));
    }
}
