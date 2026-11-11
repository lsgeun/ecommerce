package io.github.lsgeun.ecommerce.product.infrastructure;

import io.github.lsgeun.ecommerce.product.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductJpaCustomRepository {

    boolean existsByNumber(String number);

    // 재고를 동시에 차감하는 주문끼리 값이 덮어써지지 않도록 행에 쓰기 락을 건다
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.number = :number")
    Optional<Product> findByNumberForUpdate(@Param("number") String number);
}
