package io.github.lsgeun.ecommerce.product.infrastructure;

import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductStatus;
import io.github.lsgeun.ecommerce.support.annotation.SliceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// ProductJpaCustomRepositoryImpl은 일반 @Component이므로 @DataJpaTest 컨텍스트에 자동으로 포함되지 않아 별도로 Import한다
@Import(ProductJpaCustomRepositoryImpl.class)
@DataJpaTest
@SliceTest
class ProductJpaRepositorySliceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private Product createProduct(String number) {
        return Product.builder()
            .number(number)
            .name("테스트상품")
            .price(10_000L)
            .stock(5)
            .status(ProductStatus.SELLING)
            .build();
    }

    @Test
    @DisplayName("성공: 존재하는 상품 번호로 existsByNumber를 호출하면 true를 반환한다")
    void existsByNumber_존재하는_번호면_true() {
        // given
        testEntityManager.persistAndFlush(createProduct("P001"));

        // when
        boolean exists = productJpaRepository.existsByNumber("P001");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 번호로 existsByNumber를 호출하면 false를 반환한다")
    void existsByNumber_존재하지_않는_번호면_false() {
        // given - (저장된 상품 없음)

        // when
        boolean exists = productJpaRepository.existsByNumber("P999");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("성공: 존재하는 상품 번호로 findByNumberWithCache를 호출하면 상품을 반환한다")
    void findByNumberWithCache_존재하는_번호면_상품_반환() {
        // given - 영속성 컨텍스트 1차 캐시가 아닌 자연 식별자 조회 경로를 검증하기 위해 컨텍스트를 비운다
        testEntityManager.persistAndFlush(createProduct("P001"));
        testEntityManager.clear();

        // when
        Optional<Product> foundProduct = productJpaRepository.findByNumberWithCache("P001");

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getNumber()).isEqualTo("P001");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 번호로 findByNumberWithCache를 호출하면 빈 Optional을 반환한다")
    void findByNumberWithCache_존재하지_않는_번호면_빈_옵셔널_반환() {
        // given - (저장된 상품 없음)

        // when
        Optional<Product> foundProduct = productJpaRepository.findByNumberWithCache("P999");

        // then
        assertThat(foundProduct).isEmpty();
    }
}
