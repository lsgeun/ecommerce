package io.github.lsgeun.ecommerce.domain.product.exception;

import io.github.lsgeun.ecommerce.exception.BusinessException;
import io.github.lsgeun.ecommerce.exception.ErrorCode;

public abstract class ProductException extends BusinessException {
    protected ProductException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected ProductException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
