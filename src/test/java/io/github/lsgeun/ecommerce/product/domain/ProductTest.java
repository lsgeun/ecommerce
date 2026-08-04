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
        // given - (파라미터 주입으로 준비 완료)

        // when & then - 유효하지 않은 상품 번호 검증 시 예외가 발생해야 한다
        assertThatThrownBy(() -> Product.validateNumber(invalidNumber))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("상품 번호가 유효하면 예외가 발생하지 않는다")
    void validateNumber_유효하면_통과() {
        // given
        String validNumber = "P001";

        // when & then - 예외 없이 정상 수행되어야 한다
        Product.validateNumber(validNumber);
    }

    @Test
    @DisplayName("가격이 음수이면 예외가 발생한다")
    void validatePrice_음수이면_예외() {
        // given
        long invalidPrice = -1;

        // when & then - 음수 가격 검증 시 예외가 발생해야 한다
        assertThatThrownBy(() -> Product.validatePrice(invalidPrice))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("updateFrom으로 이름과 가격을 변경할 수 있다")
    void updateFrom_이름과_가격_변경() {
        // given - 변경 대상 상품과 새로운 정보를 담은 상품 객체 준비
        Product product = validProduct();
        Product updateSource = Product.builder()
            .number("P001")
            .name("변경된상품")
            .price(20_000L)
            .stock(5)
            .status(ProductStatus.SELLING)
            .build();

        // when - 상품 정보 업데이트 수행
        product.updateFrom(updateSource);

        // then - 이름과 가격이 정상적으로 변경되었는지 검증
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