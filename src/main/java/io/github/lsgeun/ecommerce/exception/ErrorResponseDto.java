package io.github.lsgeun.ecommerce.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorResponseDto(
    String message,
    String code,
    int status,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
        timezone = "Asia/Seoul"
    )
    LocalDateTime timestamp
) {

    private static final Logger log =
        LoggerFactory.getLogger(ErrorResponseDto.class);

    public static ErrorResponseDto from(
        ErrorCodeSpec errorCodeSpec
    ) {
        LocalDateTime errorTime =
            LocalDateTime.now(
                ZoneId.of("Asia/Seoul")
            );

        if (errorCodeSpec == null) {
            errorCodeSpec = ErrorCode.UNEXPECTED_SERVER_ERROR;

            log.error(
                "[ERROR] errorCode가 null입니다. 발생시각: {}",
                errorTime
            );
        }

        return builder()
            .message(errorCodeSpec.getMessage())
            .code(errorCodeSpec.getCode())
            .status(errorCodeSpec.getHttpStatus().value())
            .timestamp(errorTime)
            .build();
    }
}
