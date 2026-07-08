package io.github.lsgeun.ecommerce.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleExceptionBusinessException(
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.INVALID_INPUT_ERROR;

        logByLevel(
            errorCodeSpec.getLogLevel(),
            exception.getMessage(),
            exception
        );

        List<ErrorResponse.FieldErrorDetail> errors =
            exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError ->
                ErrorResponse.FieldErrorDetail.builder()
                .field(fieldError.getField())
                .reason(fieldError.getDefaultMessage())
                .build()
            )
            .collect(Collectors.toList());

        return createErrorResponseWithErrors(errorCodeSpec, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.INVALID_INPUT_ERROR;

        logByLevel(
            errorCodeSpec.getLogLevel(),
            exception.getMessage(),
            exception
        );

        List<ErrorResponse.FieldErrorDetail> errors =
            exception.getConstraintViolations()
            .stream()
            .map(violation -> {
                String propertyPath = violation.getPropertyPath().toString();
                String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);

                return ErrorResponse.FieldErrorDetail.builder()
                    .field(fieldName)
                    .reason(violation.getMessage())
                    .build();
            })
            .collect(Collectors.toList());

        return createErrorResponseWithErrors(errorCodeSpec, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
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

    private ResponseEntity<ErrorResponse> createErrorResponse(
        ErrorCodeSpec errorCodeSpec
    ) {
        ErrorResponse errorResponse =
            ErrorResponse.from(errorCodeSpec);

        return ResponseEntity
            .status(errorCodeSpec.getHttpStatus())
            .body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseWithErrors(
        ErrorCodeSpec errorCodeSpec,
        List<ErrorResponse.FieldErrorDetail> errors
    ) {
        ErrorResponse errorResponse =
            ErrorResponse.of(errorCodeSpec, errors);

        return ResponseEntity
            .status(errorCodeSpec.getHttpStatus())
            .body(errorResponse);
    }
}
