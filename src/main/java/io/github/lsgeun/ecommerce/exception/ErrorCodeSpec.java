package io.github.lsgeun.ecommerce.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public interface ErrorCodeSpec {
    String getMessage();
    String getCode();
    HttpStatus getHttpStatus();
    LogLevel getLogLevel();
}
