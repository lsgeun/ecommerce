package io.github.lsgeun.ecommerce.exception;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(Long productId) {
        super(ErrorCode.PRODUCT_NOT_FOUND, "productId: " + productId);
    }
}
