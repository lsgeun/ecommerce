package io.github.lsgeun.ecommerce.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected BusinessException(ErrorCode errorCode, String detailMessage) {
        super(String.format("%s (%s)", errorCode.getMessage(), detailMessage));
        this.errorCode = errorCode;
    }
}
