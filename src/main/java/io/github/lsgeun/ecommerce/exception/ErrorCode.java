package io.github.lsgeun.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeSpec {
    ENTITY_NOT_FOUND_ERROR("자원을 찾을 수 없습니다", "C001", HttpStatus.NOT_FOUND, LogLevel.WARN),
    INVALID_INPUT_ERROR("유효하지 않은 입력값입니다", "C002", HttpStatus.BAD_REQUEST, LogLevel.WARN),

    UNEXPECTED_SERVER_ERROR("서버 오류가 발생했습니다", "S001", HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR);

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final LogLevel logLevel;
}
