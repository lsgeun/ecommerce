package io.github.lsgeun.ecommerce.order.application;

import io.github.lsgeun.ecommerce.common.exception.business.ConcurrentModificationConflictException;
import io.github.lsgeun.ecommerce.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

// 트랜잭션 밖에서 재시도해야 매 시도마다 새 트랜잭션과 최신 주문 상태로 시작하므로 별도 빈으로 분리
@RequiredArgsConstructor
@Service
public class OrderCancelRetryService {

    static final int MAX_ATTEMPTS = 3;

    private final OrderSimpleService orderSimpleService;

    public Order cancelOrder(Long id) {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                return orderSimpleService.cancelOrder(id);
            } catch (OptimisticLockingFailureException exception) {
                // 충돌했다면 상대 트랜잭션은 이미 커밋을 끝냈으므로 대기 없이 바로 다시 시도해도 최신 상태를 읽음
                if (attempt == MAX_ATTEMPTS) {
                    throw new ConcurrentModificationConflictException("Order", id, MAX_ATTEMPTS);
                }
            }
        }

        throw new IllegalStateException("재시도 반복문은 반환하거나 예외를 던지고 끝나야 합니다.");
    }
}
