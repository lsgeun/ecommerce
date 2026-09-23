package io.github.lsgeun.ecommerce.product.application;

import io.github.lsgeun.ecommerce.global.exception.DomainEntityAlreadyExistsException;
import io.github.lsgeun.ecommerce.global.exception.DomainEntityNotFoundException;
import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import io.github.lsgeun.ecommerce.product.domain.ProductStatus;
import io.github.lsgeun.ecommerce.support.annotation.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class ProductSimpleServiceUnitTest {

    private FakeProductRepository productRepository;
    private ProductSimpleService productSimpleService;

    @BeforeEach
    void setUp() {
        productRepository = new FakeProductRepository();
        productSimpleService = new ProductSimpleService(productRepository);
    }

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
    @DisplayName("성공: 존재하는 상품 번호로 조회하면 해당 상품을 반환한다")
    void getProduct_존재하는_번호_조회_성공() {
        // given
        productRepository.create(createProduct("P001"));

        // when
        Product foundProduct = productSimpleService.getProduct("P001");

        // then
        assertThat(foundProduct.getNumber()).isEqualTo("P001");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 상품 번호로 조회하면 예외가 발생한다")
    void getProduct_존재하지_않는_번호면_예외() {
        // given - (저장된 상품 없음)

        // when & then
        assertThatThrownBy(() -> productSimpleService.getProduct("P999"))
            .isInstanceOf(DomainEntityNotFoundException.class);
    }

    @Test
    @DisplayName("성공: 새로운 상품 번호로 생성하면 저장소에 저장된다")
    void createProduct_새로운_번호면_생성_성공() {
        // given
        Product product = createProduct("P001");

        // when
        Product createdProduct = productSimpleService.createProduct(product);

        // then
        assertThat(productRepository.existsByNumber("P001")).isTrue();
        assertThat(createdProduct.getNumber()).isEqualTo("P001");
    }

    @Test
    @DisplayName("실패: 이미 존재하는 상품 번호로 생성하면 예외가 발생한다")
    void createProduct_중복된_번호면_예외() {
        // given
        productRepository.create(createProduct("P001"));

        // when & then
        assertThatThrownBy(() -> productSimpleService.createProduct(createProduct("P001")))
            .isInstanceOf(DomainEntityAlreadyExistsException.class);
    }

    @Test
    @DisplayName("성공: 상품 정보를 수정하면 이름과 가격이 변경된다")
    void updateProduct_이름과_가격_변경_성공() {
        // given
        productRepository.create(createProduct("P001"));
        Product updateSource = Product.builder()
            .number("P001")
            .name("변경된상품")
            .price(20_000L)
            .stock(5)
            .status(ProductStatus.SELLING)
            .build();

        // when
        Product updatedProduct = productSimpleService.updateProduct(updateSource);

        // then
        assertThat(updatedProduct.getName()).isEqualTo("변경된상품");
        assertThat(updatedProduct.getPrice()).isEqualTo(20_000L);
    }

    @Test
    @DisplayName("성공: 상품을 삭제하면 저장소에서 제거된다")
    void deleteProduct_삭제_성공() {
        // given
        productRepository.create(createProduct("P001"));

        // when
        productSimpleService.deleteProduct("P001");

        // then
        assertThat(productRepository.existsByNumber("P001")).isFalse();
    }

    @Test
    @DisplayName("성공: 재고 증가 타입으로 요청하면 재고가 증가한다")
    void updateStock_증가_타입이면_재고_증가() {
        // given
        productRepository.create(createProduct("P001"));

        // when
        Product updatedProduct =
            productSimpleService.updateStock("P001", 3, Product.StockUpdateType.INCREASE);

        // then
        assertThat(updatedProduct.getStock()).isEqualTo(8);
    }

    @Test
    @DisplayName("성공: 재고 감소 타입으로 요청하면 재고가 감소한다")
    void updateStock_감소_타입이면_재고_감소() {
        // given
        productRepository.create(createProduct("P001"));

        // when
        Product updatedProduct =
            productSimpleService.updateStock("P001", 2, Product.StockUpdateType.DECREASE);

        // then
        assertThat(updatedProduct.getStock()).isEqualTo(3);
    }

    // 외부 시스템 경계도 비결정적 요인도 아니므로, Mockito 대신 메모리 기반 Fake 구현체로 대체한다
    private static class FakeProductRepository implements ProductRepository {

        private final Map<String, Product> store = new LinkedHashMap<>();
        private final AtomicLong sequence = new AtomicLong(1);

        @Override
        public Optional<Product> findByNumber(String number) {
            return Optional.ofNullable(store.get(number));
        }

        @Override
        public Product getByNumber(String number) {
            return findByNumber(number)
                .orElseThrow(() -> new DomainEntityNotFoundException("Product", number));
        }

        @Override
        public Product create(Product product) {
            Product persistedProduct = Product.builder()
                .id(sequence.getAndIncrement())
                .number(product.getNumber())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus())
                .build();

            store.put(persistedProduct.getNumber(), persistedProduct);

            return persistedProduct;
        }

        @Override
        public Product update(Product product) {
            store.put(product.getNumber(), product);

            return product;
        }

        @Override
        public Product delete(Product product) {
            return deleteByNumber(product.getNumber());
        }

        @Override
        public Product deleteByNumber(String number) {
            Product deletedProduct = getByNumber(number);

            store.remove(number);

            return deletedProduct;
        }

        @Override
        public boolean existsByNumber(String number) {
            return store.containsKey(number);
        }
    }
}
