package io.github.lsgeun.ecommerce.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDto(
    String message,
    String errorCode,
    String status,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        timezone = "Asia/Seoul"
    )
    LocalDateTime timestamp
) {}
