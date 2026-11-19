package io.github.lsgeun.ecommerce.common.exception.business;

import io.github.lsgeun.ecommerce.common.exception.BusinessException;
import io.github.lsgeun.ecommerce.common.exception.code.ErrorCode;

// 현재 상태에서는 허용되지 않는 도메인 동작을 요청했을 때 사용
public class InvalidDomainStateException extends BusinessException {

    public InvalidDomainStateException(Class<?> targetClass, Object id, String reason) {
        super(ErrorCode.INVALID_DOMAIN_STATE_ERROR, generateMessage(targetClass, id, reason));
    }

    private static String generateMessage(Class<?> targetClass, Object id, String reason) {
        return String.format("도메인: %s, 식별자: %s, 이유: %s", targetClass.getSimpleName(), id, reason);
    }
}
