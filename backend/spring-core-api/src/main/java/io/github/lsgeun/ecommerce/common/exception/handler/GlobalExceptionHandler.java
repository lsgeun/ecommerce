package io.github.lsgeun.ecommerce.common.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.lsgeun.ecommerce.common.exception.BusinessException;
import io.github.lsgeun.ecommerce.common.exception.business.InvalidDomainFieldException;
import io.github.lsgeun.ecommerce.common.exception.code.ErrorCode;
import io.github.lsgeun.ecommerce.common.exception.code.ErrorCodeSpec;
import io.github.lsgeun.ecommerce.common.exception.code.LogLevel;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logException = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorCodeSpec errorCodeSpec = exception.getErrorCodeSpec();

        logException(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);

        return createErrorResponse(errorCodeSpec);
    }

    @ExceptionHandler(InvalidDomainFieldException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDomainFieldException(InvalidDomainFieldException exception) {
        ErrorCodeSpec errorCodeSpec = exception.getErrorCodeSpec();

        logException(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);

        List<ErrorResponse.FieldErrorDetail> fieldErrorDetails =
            List.of(new ErrorResponse.FieldErrorDetail(exception.getField(), exception.getReason()));

        return createErrorResponseWithFieldErrorDetails(errorCodeSpec, fieldErrorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.INVALID_INPUT_ERROR;

        logException(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);

        List<ErrorResponse.FieldErrorDetail> fieldErrorDetails =
            exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ErrorResponse.FieldErrorDetail(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());

        return createErrorResponseWithFieldErrorDetails(errorCodeSpec, fieldErrorDetails);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.INVALID_INPUT_ERROR;

        logException(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);

        List<ErrorResponse.FieldErrorDetail> fieldErrorDetails =
            exception.getConstraintViolations()
            .stream()
            .map(violation -> {
                String propertyPath = violation.getPropertyPath().toString();
                String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);

                return new ErrorResponse.FieldErrorDetail(fieldName, violation.getMessage());
            })
            .collect(Collectors.toList());

        return createErrorResponseWithFieldErrorDetails(errorCodeSpec, fieldErrorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorCodeSpec errorCodeSpec = ErrorCode.UNEXPECTED_SERVER_ERROR;

        logException(errorCodeSpec.getLogLevel(), exception.getMessage(), exception);

        return createErrorResponse(errorCodeSpec);
    }

    private void logException(LogLevel logLevel, String message, Exception exception) {
        
        // 메시지가 없는 경우, 메시지에 예외 메시지, 예외 이름 순으로 할당됨
        if (message == null) {
            if (exception.getMessage() != null) {
                message = exception.getMessage();
            } else {
                message = exception.getClass().getName();
            }
        }
        
        switch (logLevel) {
            case INFO: logException.info(message); break;
            case WARN: logException.warn(message); break;
            // 에러만 스택 트레이스 남김
            case ERROR: logException.error(message, exception); break;
            default: break;
        }
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(ErrorCodeSpec errorCodeSpec) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCodeSpec);

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseWithFieldErrorDetails(
        ErrorCodeSpec errorCodeSpec, List<ErrorResponse.FieldErrorDetail> fieldErrorDetails
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCodeSpec, fieldErrorDetails);

        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
