package io.github.lsgeun.ecommerce.common.exception.code;

public interface ErrorCodeSpec {

    String getMessage();

    String getCode();

    int getHttpStatusCode();

    LogLevel getLogLevel();
}
