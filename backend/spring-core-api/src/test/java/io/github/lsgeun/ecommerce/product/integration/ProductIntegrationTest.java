package io.github.lsgeun.ecommerce.product.integration;

import io.github.lsgeun.ecommerce.global.exception.DomainEntityAlreadyExistsException;
import io.github.lsgeun.ecommerce.global.exception.DomainEntityNotFoundException;
import io.github.lsgeun.ecommerce.product.application.ProductSimpleService;
import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import io.github.lsgeun.ecommerce.product.domain.ProductStatus;
import io.github.lsgeun.ecommerce.support.annotation.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// 실제 웹 환경을 띄우지 않는 통합 테스트이므로, @Transactional 롤백으로 DB 상태를 격리한다
@Transactional
@IntegrationTest
class ProductIntegrationTest {

    @Autowired
    private ProductSimpleService productSimpleService;

    @Autowired
    private ProductRepository productRepository;

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
    @DisplayName("성공: 상품을 생성하면 저장소를 통해 동일한 번호로 조회된다")
    void createProduct_생성한_상품_조회_가능() {
        // given
        Product product = createProduct("P001");

        // when
        Product createdProduct = productSimpleService.createProduct(product);

        // then
        Product foundProduct = productSimpleService.getProduct("P001");
        assertThat(foundProduct.getId()).isEqualTo(createdProduct.getId());
        assertThat(foundProduct.getName()).isEqualTo("테스트상품");
    }

    @Test
    @DisplayName("실패: 이미 존재하는 상품 번호로 생성하면 예외가 발생한다")
    void createProduct_중복된_번호면_예외() {
        // given
        productSimpleService.createProduct(createProduct("P001"));

        // when & then
        assertThatThrownBy(() -> productSimpleService.createProduct(createProduct("P001")))
            .isInstanceOf(DomainEntityAlreadyExistsException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 번호를 조회하면 예외가 발생한다")
    void getProduct_존재하지_않는_번호면_예외() {
        // given - (저장된 상품 없음)

        // when & then
        assertThatThrownBy(() -> productSimpleService.getProduct("P999"))
            .isInstanceOf(DomainEntityNotFoundException.class);
    }

    @Test
    @DisplayName("성공: 상품 정보를 수정하면 변경 사항이 저장소에 반영된다")
    void updateProduct_변경사항_저장소에_반영() {
        // given
        productSimpleService.createProduct(createProduct("P001"));
        Product updateSource = Product.builder()
            .number("P001")
            .name("변경된상품")
            .price(20_000L)
            .stock(3)
            .status(ProductStatus.SOLD_OUT)
            .build();

        // when
        productSimpleService.updateProduct(updateSource);

        // then
        Product foundProduct = productSimpleService.getProduct("P001");
        assertThat(foundProduct.getName()).isEqualTo("변경된상품");
        assertThat(foundProduct.getPrice()).isEqualTo(20_000L);
        assertThat(foundProduct.getStatus()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("성공: 상품을 삭제하면 저장소에서 더 이상 조회되지 않는다")
    void deleteProduct_삭제후_조회_불가() {
        // given
        productSimpleService.createProduct(createProduct("P001"));

        // when
        productSimpleService.deleteProduct("P001");

        // then
        assertThat(productRepository.existsByNumber("P001")).isFalse();
    }

    @Test
    @DisplayName("성공: 재고를 증가시키면 증가한 수량이 저장소에 반영된다")
    void updateStock_증가한_수량_저장소에_반영() {
        // given
        productSimpleService.createProduct(createProduct("P001"));

        // when
        productSimpleService.updateStock("P001", 3, Product.StockUpdateType.INCREASE);

        // then
        Product foundProduct = productSimpleService.getProduct("P001");
        assertThat(foundProduct.getStock()).isEqualTo(8);
    }
}
