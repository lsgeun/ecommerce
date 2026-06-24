package io.github.lsgeun.ecommerce.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleExceptionBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        logByLevel(errorCode.getLogLevel(), e.getMessage(), e);
        return createErrorResponse(errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.UNEXPECTED_SERVER_ERROR;
        logByLevel(errorCode.getLogLevel(), e.getMessage(), e);
        return createErrorResponse(errorCode);
    }

    // LogLevel 수준으로 로그 출력
    private void logByLevel(LogLevel logLevel, String message, Exception e) {
        switch (logLevel) {
            case TRACE:
                logger.trace(message, e);
                break;
            case DEBUG:
                logger.debug(message, e);
                break;
            case INFO:
                logger.info(message, e);
                break;
            case WARN:
                logger.warn(message, e);
                break;
            case ERROR:
                logger.error(message, e);
                break;
            default:
                break;
        }
    }

    // 매퍼 역할, ErrorCode를 이용해 ErrorDto를 담는 ResponseEntity 생성
    private ResponseEntity<ErrorDto> createErrorResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorDto.builder()
                        .message(errorCode.getMessage())
                        .errorCode(errorCode.getCode())
                        .build());
    }
}
