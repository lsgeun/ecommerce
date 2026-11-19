package io.github.lsgeun.ecommerce.integration;

import io.github.lsgeun.ecommerce.common.exception.business.InvalidDomainFieldException;
import io.github.lsgeun.ecommerce.order.application.OrderItemCommand;
import io.github.lsgeun.ecommerce.order.application.OrderSimpleService;
import io.github.lsgeun.ecommerce.order.infrastructure.OrderJpaRepository;
import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.infrastructure.ProductJpaRepository;
import io.github.lsgeun.ecommerce.support.annotation.IntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// 여러 스레드가 같은 상품의 재고를 동시에 차감할 때 재고 정합성이 지켜지는지 검증한다.
// 락을 제거하거나 다른 방식으로 바꾼 뒤에도 같은 기준으로 동시성 문제를 다시 확인하는 용도다.
// 트랜잭션 롤백으로는 다른 스레드의 커밋을 재현할 수 없으므로 @Transactional을 쓰지 않고 직접 정리한다.
@SpringBootTest
@IntegrationTest
@RequiredArgsConstructor
class CreateOrderConcurrencyIntegrationTest {

    private static final String PRODUCT_NUMBER = "P-CONCURRENCY";
    private static final long USER_ID = 1L;
    private static final long TASK_TIMEOUT_SECONDS = 30L;

    private final OrderSimpleService orderSimpleService;
    private final ProductJpaRepository productJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    @AfterEach
    void cleanUp() {
        orderJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
    }

    private Long saveProduct(int stock) {
        return productJpaRepository
            .save(Product.create(PRODUCT_NUMBER, "동시성상품", 10_000L, stock))
            .getId();
    }

    // 모든 스레드가 준비를 마친 뒤 한꺼번에 출발시켜 실행 시점의 겹침을 최대화한다
    private ConcurrentResult runSimultaneously(int threadCount, Runnable task) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < threadCount; i++) {
                futures.add(executor.submit(() -> {
                    readyLatch.countDown();
                    startLatch.await();
                    task.run();
                    return null;
                }));
            }

            readyLatch.await();
            startLatch.countDown();

            int successCount = 0;
            List<Throwable> failures = new ArrayList<>();
            for (Future<?> future : futures) {
                try {
                    future.get(TASK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    successCount++;
                } catch (ExecutionException e) {
                    failures.add(e.getCause());
                }
            }

            return new ConcurrentResult(successCount, failures);
        } finally {
            executor.shutdownNow();
        }
    }

    private record ConcurrentResult(int successCount, List<Throwable> failures) {
    }

    @Nested
    @DisplayName("createOrder 동시 호출")
    class CreateOrderConcurrently {

        @Test
        @DisplayName("성공: 재고와 같은 수만큼 동시에 주문하면 모두 성공하고 재고가 정확히 0이 된다")
        void 재고만큼_동시_주문하면_재고가_정확히_0() throws Exception {
            // given
            int threadCount = 30;
            Long productId = saveProduct(threadCount);
            List<OrderItemCommand> commands = List.of(new OrderItemCommand(PRODUCT_NUMBER, 1));

            // when
            ConcurrentResult result = runSimultaneously(
                threadCount,
                () -> orderSimpleService.createOrder(USER_ID, commands)
            );

            // then
            Product product = productJpaRepository.findById(productId).orElseThrow();
            assertThat(result.failures()).isEmpty();
            assertThat(result.successCount()).isEqualTo(threadCount);
            assertThat(orderJpaRepository.count()).isEqualTo(threadCount);
            assertThat(product.getStock()).isZero();
        }

        @Test
        @DisplayName("실패: 재고보다 많이 동시에 주문하면 재고만큼만 성공하고 나머지는 재고 부족으로 실패한다")
        void 재고보다_많이_동시_주문하면_초과분은_실패() throws Exception {
            // given
            int stock = 10;
            int threadCount = 30;
            Long productId = saveProduct(stock);
            List<OrderItemCommand> commands = List.of(new OrderItemCommand(PRODUCT_NUMBER, 1));

            // when
            ConcurrentResult result = runSimultaneously(
                threadCount,
                () -> orderSimpleService.createOrder(USER_ID, commands)
            );

            // then
            Product product = productJpaRepository.findById(productId).orElseThrow();
            assertThat(result.successCount()).isEqualTo(stock);
            assertThat(result.failures())
                .hasSize(threadCount - stock)
                .allSatisfy(failure -> assertThat(failure).isInstanceOf(InvalidDomainFieldException.class));
            assertThat(orderJpaRepository.count()).isEqualTo(stock);
            assertThat(product.getStock()).isZero();
        }
    }
}
