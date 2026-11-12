package io.github.lsgeun.ecommerce.product.domain;

import io.github.lsgeun.ecommerce.common.exception.business.InvalidDomainFieldException;
import io.github.lsgeun.ecommerce.support.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class ProductTest {

    private Product validProduct() {
        return createProduct(5);
    }

    private Product createProduct(int stock) {
        return Product.builder()
            .number("P001")
            .name("테스트상품")
            .price(10_000L)
            .stock(stock)
            .build();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("provideInvalidNumbers")
    @DisplayName("상품 번호가 유효하지 않으면 예외가 발생한다")
    void validateNumber_유효하지_않으면_예외(String invalidNumber) {
        // given - (파라미터 주입으로 준비 완료)

        // when & then
        assertThatThrownBy(() -> Product.validateNumber(invalidNumber))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("상품 번호가 유효하면 예외가 발생하지 않는다")
    void validateNumber_유효하면_통과() {
        // given
        String validNumber = "P001";

        // when & then
        Product.validateNumber(validNumber);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("provideInvalidNames")
    @DisplayName("상품 이름이 유효하지 않으면 예외가 발생한다")
    void validateName_유효하지_않으면_예외(String invalidName) {
        // given - (파라미터 주입으로 준비 완료)

        // when & then
        assertThatThrownBy(() -> Product.validateName(invalidName))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("가격이 음수이면 예외가 발생한다")
    void validatePrice_음수이면_예외() {
        // given
        long invalidPrice = -1;

        // when & then
        assertThatThrownBy(() -> Product.validatePrice(invalidPrice))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("재고가 0이거나 999이면 검증을 통과한다")
    void validateStock_경계값이면_통과() {
        // given - (경계값 0, 999)

        // when & then
        Product.validateStock(0);
        Product.validateStock(999);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1000})
    @DisplayName("재고가 0 미만이거나 999를 초과하면 예외가 발생한다")
    void validateStock_범위를_벗어나면_예외(int invalidStock) {
        // given - (파라미터 주입으로 준비 완료)

        // when & then
        assertThatThrownBy(() -> Product.validateStock(invalidStock))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("decreaseStock으로 재고를 줄이면 감소한 수량이 반영된다")
    void decreaseStock_재고_감소() {
        // given
        Product product = validProduct();

        // when
        product.decreaseStock(2);

        // then
        assertThat(product.getStock()).isEqualTo(3);
    }

    @Test
    @DisplayName("decreaseStock으로 재고를 정확히 0까지 줄이면 정상 처리된다")
    void decreaseStock_0까지_감소_성공() {
        // given
        Product product = createProduct(2);

        // when
        product.decreaseStock(2);

        // then
        assertThat(product.getStock()).isZero();
    }

    @Test
    @DisplayName("decreaseStock으로 보유 재고보다 많은 수량을 줄이면 예외가 발생하고 재고는 그대로다")
    void decreaseStock_재고보다_많으면_예외() {
        // given
        Product product = validProduct();

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(product.getStock() + 1))
            .isInstanceOf(InvalidDomainFieldException.class);
        assertThat(product.getStock()).isEqualTo(5);
    }

    // 테스트에 주입할 파라미터 공급 메서드 (static 필수)
    private static Stream<String> provideInvalidNumbers() {
        return Stream.of(
            "A",                  // 1자 (너무 짧음)
            "가".repeat(51)        // 51자 (너무 긺)
        );
    }

    private static Stream<String> provideInvalidNames() {
        return Stream.of(
            "A",                  // 1자 (너무 짧음)
            "가".repeat(51)        // 51자 (너무 긺)
        );
    }
}
