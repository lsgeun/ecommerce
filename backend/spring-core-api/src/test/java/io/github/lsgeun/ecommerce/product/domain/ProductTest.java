package io.github.lsgeun.ecommerce.product.domain;

import io.github.lsgeun.ecommerce.global.exception.InvalidDomainFieldException;
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

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("provideInvalidNames")
    @DisplayName("상품 이름이 유효하지 않으면 예외가 발생한다")
    void validateName_유효하지_않으면_예외(String invalidName) {
        // given - (파라미터 주입으로 준비 완료)

        // when & then - 유효하지 않은 상품 이름 검증 시 예외가 발생해야 한다
        assertThatThrownBy(() -> Product.validateName(invalidName))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1000})
    @DisplayName("재고가 0 미만이거나 999를 초과하면 예외가 발생한다")
    void validateStock_범위를_벗어나면_예외(int invalidStock) {
        // given - (파라미터 주입으로 준비 완료)

        // when & then - 범위를 벗어난 재고 검증 시 예외가 발생해야 한다
        assertThatThrownBy(() -> Product.validateStock(invalidStock))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("상품 상태가 null이면 예외가 발생한다")
    void validateStatus_null이면_예외() {
        // given & when & then - null 상태 검증 시 예외가 발생해야 한다
        assertThatThrownBy(() -> Product.validateStatus(null))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("increaseStock으로 재고를 늘릴 수 있다")
    void increaseStock_재고_증가() {
        // given
        Product product = validProduct();

        // when - 재고 3개 증가
        product.increaseStock(3);

        // then - 증가한 재고 수량이 반영되었는지 검증
        assertThat(product.getStock()).isEqualTo(8);
    }

    @Test
    @DisplayName("increaseStock으로 재고가 999를 초과하면 예외가 발생한다")
    void increaseStock_상한_초과시_예외() {
        // given
        Product product = Product.builder()
            .number("P001")
            .name("테스트상품")
            .price(10_000L)
            .stock(999)
            .status(ProductStatus.SELLING)
            .build();

        // when & then - 상한을 초과하는 재고 증가 시 예외가 발생해야 한다
        assertThatThrownBy(() -> product.increaseStock(1))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("decreaseStock으로 재고를 줄일 수 있다")
    void decreaseStock_재고_감소() {
        // given
        Product product = validProduct();

        // when - 재고 2개 감소
        product.decreaseStock(2);

        // then - 감소한 재고 수량이 반영되었는지 검증
        assertThat(product.getStock()).isEqualTo(3);
    }

    @Test
    @DisplayName("decreaseStock으로 재고보다 많은 수량을 감소시키면 예외가 발생한다")
    void decreaseStock_재고보다_많으면_예외() {
        // given
        Product product = validProduct();

        // when & then - 보유 재고보다 많은 수량 감소 시 예외가 발생해야 한다
        assertThatThrownBy(() -> product.decreaseStock(product.getStock() + 1))
            .isInstanceOf(InvalidDomainFieldException.class);
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