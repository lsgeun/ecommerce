package io.github.lsgeun.ecommerce.common.exception.business;

import lombok.Getter;

import java.util.List;

import io.github.lsgeun.ecommerce.common.exception.BusinessException;
import io.github.lsgeun.ecommerce.common.exception.code.ErrorCode;
import io.github.lsgeun.ecommerce.common.exception.handler.ErrorResponse;

@Getter
public class InvalidDomainFieldException extends BusinessException {

    private final transient List<ErrorResponse.FieldErrorDetail> fieldErrorDetails;

    public InvalidDomainFieldException(Class<?> targetClass, String field, Object inputValue, String reason) {
        super(ErrorCode.INVALID_INPUT_ERROR, generateMessage(targetClass, field, inputValue, reason));
        this.fieldErrorDetails = List.of(new ErrorResponse.FieldErrorDetail(field, reason));
    }

    private static String generateMessage(Class<?> targetClass, String field, Object inputValue, String reason) {
        return String.format(
            "도메인: %s, 필드: %s, 값: %s, 이유: %s",
            targetClass.getSimpleName(), field, inputValue, reason
        );
    }
}
