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
    public ResponseEntity<ErrorDto> handleExceptionBusinessException(BusinessException exception) {
        ErrorCodeSpec errorCodeSpec = exception.getErrorCodeSpec();
        logByLevel(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);
        return createErrorResponse(errorCodeSpec);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.UNEXPECTED_SERVER_ERROR;
        logByLevel(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);
        return createErrorResponse(errorCodeSpec);
    }

    // LogLevel 수준으로 로그 출력
    private void logByLevel(LogLevel logLevel, String message, Exception exception) {
        switch (logLevel) {
            case TRACE:
                logger.trace(message, exception);
                break;
            case DEBUG:
                logger.debug(message, exception);
                break;
            case INFO:
                logger.info(message, exception);
                break;
            case WARN:
                logger.warn(message, exception);
                break;
            case ERROR:
                logger.error(message, exception);
                break;
            default:
                break;
        }
    }

    // 매퍼 역할, ErrorCodeSpec를 이용해 ErrorDto를 담는 ResponseEntity 생성
    private ResponseEntity<ErrorDto> createErrorResponse(ErrorCodeSpec errorCodeSpec) {
        return ResponseEntity
            .status(errorCodeSpec.getHttpStatus())
            .body(ErrorDto.builder()
                .message(errorCodeSpec.getMessage())
                .errorCodeSpec(errorCodeSpec.getCode())
                .build());
    }
}
