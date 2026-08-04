package io.github.lsgeun.ecommerce.product.application;

import io.github.lsgeun.ecommerce.global.exception.DomainEntityAlreadyExistsException;
import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import io.github.lsgeun.ecommerce.product.domain.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProductSimpleServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductSimpleService productSimpleService;

    private Product validProduct() {
        return Product.builder()
            .number("P001")
            .name("테스트상품")
            .price(10_000L)
            .stock(5)
            .status(ProductStatus.SELLING)
            .build();
    }

    @Test
    @DisplayName("이미 존재하는 상품 번호로 생성하면 예외가 발생한다")
    void createProduct_중복번호_예외() {
        Product product = validProduct();
        given(productRepository.existsByNumber("P001")).willReturn(true);

        assertThatThrownBy(() -> productSimpleService.createProduct(product))
            .isInstanceOf(DomainEntityAlreadyExistsException.class);

        then(productRepository).should(never()).create(any());
    }

    @Test
    @DisplayName("새 상품을 정상적으로 생성한다")
    void createProduct_성공() {
        Product product = validProduct();
        given(productRepository.existsByNumber("P001")).willReturn(false);
        given(productRepository.create(product)).willReturn(product);

        Product result = productSimpleService.createProduct(product);

        assertThat(result.getNumber()).isEqualTo("P001");
        then(productRepository).should().create(product);
    }

    @Test
    @DisplayName("상품을 수정하면 updateFrom 후 repository.update를 호출한다")
    void updateProduct_성공() {
        Product existing = validProduct();
        Product updateRequest = Product.builder()
            .number("P001")
            .name("변경된상품")
            .price(20_000L)
            .stock(3)
            .status(ProductStatus.SOLD_OUT)
            .build();

        given(productRepository.getByNumber("P001")).willReturn(existing);
        given(productRepository.update(existing)).willReturn(existing);

        productSimpleService.updateProduct(updateRequest);

        assertThat(existing.getName()).isEqualTo("변경된상품");
        assertThat(existing.getPrice()).isEqualTo(20_000L);
        then(productRepository).should().update(existing);
    }
}