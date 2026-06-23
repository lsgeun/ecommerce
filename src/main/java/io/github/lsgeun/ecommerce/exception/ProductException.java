package io.github.lsgeun.ecommerce.exception;

public abstract class ProductException extends BusinessException {
    protected ProductException(ErrorCode errorCode) {
        super(errorCode);
    }

    protected ProductException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
