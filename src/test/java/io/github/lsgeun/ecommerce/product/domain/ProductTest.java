package io.github.lsgeun.ecommerce.product.domain;

import io.github.lsgeun.ecommerce.global.exception.InvalidDomainFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    private Product validProduct() {
        return Product.builder()
            .number("P001")
            .name("테스트상품")
            .price(10_000L)
            .stock(5)
            .status(ProductStatus.SELLING)
            .build();
      }
      
    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("provideInvalidNumbers")
    @DisplayName("상품 번호가 유효하지 않으면 예외가 발생한다")
    void validateNumber_유효하지_않으면_예외(String invalidNumber) {
        assertThatThrownBy(() -> Product.validateNumber(invalidNumber))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("상품 번호가 유효하면 예외가 발생하지 않는다")
    void validateNumber_유효하면_통과() {
        Product.validateNumber("P001"); // 예외 없으면 통과
    }

    @Test
    @DisplayName("가격이 음수이면 예외가 발생한다")
    void validatePrice_음수이면_예외() {
        assertThatThrownBy(() -> Product.validatePrice(-1))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("updateFrom으로 이름과 가격을 변경할 수 있다")
    void updateFrom_이름과_가격_변경() {
        Product product = validProduct();
        Product updateSource = Product.builder()
            .number("P001")
            .name("변경된상품")
            .price(20_000L)
            .stock(5)
            .status(ProductStatus.SELLING)
            .build();
        product.updateFrom(updateSource);
        assertThat(product.getName()).isEqualTo("변경된상품");
        assertThat(product.getPrice()).isEqualTo(20_000L);
    }
    
    // 테스트에 주입할 파라미터 공급 메서드 (static 필수)
    private static Stream<String> provideInvalidNumbers() {
        return Stream.of(
            "A",                  // 1자 (너무 짧음)
            "가".repeat(51)        // 51자 (너무 긺)
        );
    }
}
