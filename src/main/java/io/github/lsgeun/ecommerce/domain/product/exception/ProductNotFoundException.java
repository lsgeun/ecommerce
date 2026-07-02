package io.github.lsgeun.ecommerce.domain.product.exception;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(String number) {
        super(ProductErrorCode.PRODUCT_NOT_FOUND, "productNumber: " + number);
    }
}
