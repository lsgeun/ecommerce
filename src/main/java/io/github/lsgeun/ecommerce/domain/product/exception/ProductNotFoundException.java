package io.github.lsgeun.ecommerce.domain.product.exception;

import io.github.lsgeun.ecommerce.exception.ErrorCode;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(String number) {
        super(ErrorCode.PRODUCT_NOT_FOUND, "productNumber: " + number);
    }
}
