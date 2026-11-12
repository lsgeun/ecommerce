package io.github.lsgeun.ecommerce.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeSpec {

    DOMAIN_ENTITY_NOT_FOUND_ERROR("자원을 찾을 수 없습니다", "C001", Status.NOT_FOUND, LogLevel.WARN),
    INVALID_INPUT_ERROR("유효하지 않은 입력값입니다", "C002", Status.BAD_REQUEST, LogLevel.WARN),
    DOMAIN_ENTITY_ALREADY_EXISTS_ERROR("자원이 중복됩니다.", "C003", Status.CONFLICT, LogLevel.WARN),

    UNEXPECTED_SERVER_ERROR("서버 오류가 발생했습니다", "S001", Status.INTERNAL_SERVER_ERROR, LogLevel.ERROR);

    private final String message;
    private final String code;
    private final int httpStatusCode;
    private final LogLevel logLevel;

    // 상수 인자에서 이름으로 참조할 수 있도록 중첩 클래스에 모아둔 상태 코드 별칭
    private static final class Status {

        static final int BAD_REQUEST = 400;
        static final int NOT_FOUND = 404;
        static final int CONFLICT = 409;
        static final int INTERNAL_SERVER_ERROR = 500;
    }
}
