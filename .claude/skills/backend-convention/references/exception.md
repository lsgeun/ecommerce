---
updated: 2026-10-22
---

- `global/exception` 패키지의 예외 클래스, ErrorCode, ErrorResponse, GlobalExceptionHandler 규칙
- 모든 도메인이 공유하는 유일한 공용 패키지

# 구성

| 클래스                               | 역할                                                                  |
| ------------------------------------ | --------------------------------------------------------------------- |
| `ErrorCodeSpec`                      | 메시지, 코드, `HttpStatus`, `LogLevel`을 한 묶음으로 보는 인터페이스  |
| `ErrorCode`                          | `ErrorCodeSpec`을 구현한 enum. 에러 하나가 enum 값 하나                |
| `BusinessException`                  | 업무 예외의 부모 (`abstract`, `RuntimeException`)                      |
| `InvalidDomainFieldException`        | 엔티티의 값 규칙 위반                                                 |
| `DomainEntityNotFoundException`      | 식별자로 찾았는데 없음                                                |
| `DomainEntityAlreadyExistsException` | 이미 있어서 만들 수 없음                                              |
| `ErrorResponse`                      | 에러 응답 본문                                                        |
| `GlobalExceptionHandler`             | `@ControllerAdvice`. 예외를 `ErrorResponse`로 바꾸고 로그를 남김       |

# ErrorCode

- 필드는 `message`, `code`, `httpStatus`, `logLevel`
- 코드 첫 글자가 `C`이면 클라이언트 오류, `S`이면 서버 오류. 뒤는 세 자리 숫자
- 로그 레벨은 클라이언트 오류가 `WARN`, 서버 오류가 `ERROR`

| enum 값                              | 코드   | HTTP | 로그  | 언제                                          |
| ------------------------------------ | ------ | ---- | ----- | --------------------------------------------- |
| `DOMAIN_ENTITY_NOT_FOUND_ERROR`      | `C001` | 404  | WARN  | 식별자로 찾았는데 없음                        |
| `INVALID_INPUT_ERROR`                | `C002` | 400  | WARN  | 입력 형식 위반, 엔티티 값 규칙 위반           |
| `DOMAIN_ENTITY_ALREADY_EXISTS_ERROR` | `C003` | 409  | WARN  | 중복                                          |
| `UNEXPECTED_SERVER_ERROR`            | `S001` | 500  | ERROR | 처리하지 못한 모든 예외                        |

- 에러 코드는 도메인별이 아니라 종류별 공용 코드. `product`와 `user`가 같은 `C001`을 씀

# 예외 종류별 응답

| 상황                                          | 던져지는 예외                                                         | 응답                                |
| --------------------------------------------- | --------------------------------------------------------------------- | ----------------------------------- |
| 요청 본문 DTO 검증 실패                       | `MethodArgumentNotValidException`                                     | 400, `C002`, `fieldErrorDetails`    |
| 경로 변수 제약 실패                           | `ConstraintViolationException`                                        | 400, `C002`, `fieldErrorDetails`    |
| 엔티티 값 규칙 위반                           | `InvalidDomainFieldException`                                         | 400, `C002`, `fieldErrorDetails` 1건 |
| 없음                                          | `DomainEntityNotFoundException`                                       | 404, `C001`                         |
| 중복                                          | `DomainEntityAlreadyExistsException`                                  | 409, `C003`                         |
| 그 외 `BusinessException`                     | `BusinessException`의 하위 클래스                                      | 그 예외의 `ErrorCode`에 따름         |
| 위에 해당하지 않는 모든 `Exception`           | `Exception`                                                           | 500, `S001`, 스택 로그              |

- 핸들러에 없는 예외는 **전부 500**이 됨. 예: JSON 파싱 오류, 지원하지 않는 HTTP 메서드, 없는 경로, DB 유니크 제약 위반

# ErrorResponse

```json
{
    "message": "유효하지 않은 입력값입니다",
    "code": "C002",
    "status": 400,
    "timestamp": "2026-10-22T13:48:34.226",
    "fieldErrorDetails": [
        { "field": "number", "reason": "상품 번호는 2자 이상 50자 이하이어야 합니다." }
    ]
}
```

- `fieldErrorDetails`는 없으면 응답에서 빠짐 (`NON_NULL`)
- `timestamp`는 `Asia/Seoul` 시간
- 메시지, 코드, 상태 중 하나라도 비면 `UNEXPECTED_SERVER_ERROR`로 대체하고 로그를 남김
- 응답은 **반드시** `ErrorResponse.from(...)` 또는 `ErrorResponse.of(...)`로 만듦. 생성자는 `private`

# 새 업무 예외를 추가하는 순서

1. `ErrorCode`에 enum 값을 추가. 코드는 `C` 또는 `S`에 이어지는 다음 번호, `logLevel`은 클라이언트 오류면 `WARN`
2. `BusinessException`을 상속하는 클래스를 만들고 생성자에서 `super(ErrorCode.XXX, 상세 메시지)`를 호출
3. 던지는 곳에서 `throw new XxxException(...)`
4. `GlobalExceptionHandler`는 **고치지 않음**. `BusinessException` 핸들러가 `ErrorCode`의 상태와 로그 레벨을 쓰므로 자동으로 처리됨
    - 예외: 응답에 `fieldErrorDetails` 같은 추가 정보가 필요하면 `InvalidDomainFieldException`처럼 전용 핸들러를 추가

- 상세 메시지는 `BusinessException`이 `"{ErrorCode 메시지} ({상세 메시지})"`로 합쳐서 예외 메시지로 만듦. 상세 메시지는 서버 로그용이고, 응답 `message`에는 `ErrorCode`의 메시지만 나감

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- 도메인별 에러 코드 접두(`P001`, `U001`)와 도메인별 예외 클래스
- JSON 파싱 오류(400), 지원하지 않는 메서드(405), 없는 경로(404), DB 유니크 제약 위반(409)을 500이 아니게 만드는 방식
