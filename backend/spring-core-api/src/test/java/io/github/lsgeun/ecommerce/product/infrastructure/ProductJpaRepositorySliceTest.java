package io.github.lsgeun.ecommerce.product.infrastructure;

import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.support.annotation.SliceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
            .build();
    }

    @Test
    @DisplayName("성공: 존재하는 상품 번호로 findByNumberForUpdate를 호출하면 상품을 반환한다")
    void findByNumberForUpdate_존재하는_번호면_상품_반환() {
        // given
        testEntityManager.persistAndFlush(createProduct("P001"));

        // when
        Optional<Product> foundProduct = productJpaRepository.findByNumberForUpdate("P001");

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getNumber()).isEqualTo("P001");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 번호로 findByNumberForUpdate를 호출하면 빈 Optional을 반환한다")
    void findByNumberForUpdate_존재하지_않는_번호면_빈_옵셔널_반환() {
        // given - (저장된 상품 없음)

        // when
        Optional<Product> foundProduct = productJpaRepository.findByNumberForUpdate("P999");

        // then
        assertThat(foundProduct).isEmpty();
    }
}
