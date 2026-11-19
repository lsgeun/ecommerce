package io.github.lsgeun.ecommerce.common.exception.business;

import io.github.lsgeun.ecommerce.common.exception.BusinessException;
import io.github.lsgeun.ecommerce.common.exception.code.ErrorCode;

// 낙관적 락 충돌이 재시도 한도까지 계속되어 요청을 처리하지 못했을 때 사용
public class ConcurrentModificationConflictException extends BusinessException {

    public ConcurrentModificationConflictException(String name, Object id, int attempts) {
        super(ErrorCode.CONCURRENT_MODIFICATION_ERROR, generateMessage(name, id, attempts));
    }

    private static String generateMessage(String name, Object id, int attempts) {
        return String.format("도메인: %s, 식별자: %s, 시도 횟수: %d", name, id, attempts);
    }
}
