package io.github.lsgeun.ecommerce.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleExceptionBusinessException(
        BusinessException exception
    ) {
        ErrorCodeSpec errorCodeSpec = exception.getErrorCodeSpec();

        logByLevel(
            errorCodeSpec.getLogLevel(),
            exception.getMessage(),
            exception
        );

        return createErrorResponse(errorCodeSpec);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(
        Exception exception
    ) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.UNEXPECTED_SERVER_ERROR;

        logByLevel(
            errorCodeSpec.getLogLevel(),
            exception.getMessage(),
            exception
        );

        return createErrorResponse(errorCodeSpec);
    }

    private void logByLevel(
        LogLevel logLevel,
        String message,
        Exception exception
    ) {
        switch (logLevel) {
            case TRACE:
                log.trace(message, exception);
                break;
            case DEBUG:
                log.debug(message, exception);
                break;
            case INFO:
                log.info(message, exception);
                break;
            case WARN:
                log.warn(message, exception);
                break;
            case ERROR:
                log.error(message, exception);
                break;
            default:
                break;
        }
    }

    private ResponseEntity<ErrorResponseDto> createErrorResponse(
        ErrorCodeSpec errorCodeSpec
    ) {
        return ResponseEntity
            .status(errorCodeSpec.getHttpStatus())
            .body(
                ErrorResponseDto.builder()
                    .message(errorCodeSpec.getMessage())
                    .errorCode(errorCodeSpec.getCode())
                    .timestamp(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                    .build()
            );
    }
}
