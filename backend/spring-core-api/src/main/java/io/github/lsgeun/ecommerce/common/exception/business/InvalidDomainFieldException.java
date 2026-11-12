package io.github.lsgeun.ecommerce.common.exception.business;

import lombok.Getter;

import io.github.lsgeun.ecommerce.common.exception.BusinessException;
import io.github.lsgeun.ecommerce.common.exception.code.ErrorCode;

@Getter
public class InvalidDomainFieldException extends BusinessException {

    private final String field;
    private final String reason;

    public InvalidDomainFieldException(Class<?> targetClass, String field, Object inputValue, String reason) {
        super(ErrorCode.INVALID_INPUT_ERROR, generateMessage(targetClass, field, inputValue, reason));
        this.field = field;
        this.reason = reason;
    }

    private static String generateMessage(Class<?> targetClass, String field, Object inputValue, String reason) {
        return String.format(
            "도메인: %s, 필드: %s, 값: %s, 이유: %s",
            targetClass.getSimpleName(), field, inputValue, reason
        );
    }
}
