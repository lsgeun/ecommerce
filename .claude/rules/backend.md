# `backend/**/src/main/java/`

## 패키지 폴더 구조

1. 기본 패키지는 `io.github.lsgeun.ecommerce`
2. 기본 패키지 바로 아래에 **반드시** `도메인 패키지`(`product/`, `user/`, `notification/` 등), `공용 패키지`(`common/`)만 둔다.
3. 도메인 패키지 바로 아래 **반드시** 4개의 패키지(`표현 계층` `presentation/`, `응용 계층` `application/`, `도메인 계층` `domain/`, `인프라 계층` `infrastructure/`)만 둔다

화살표는 `import`하는 방향

```
presentation -> application -> domain <- infrastructure
```

## `공용 패키지`

`공용 패키지`에는 모든 도메인에서 사용되거나 백엔드 설정을 하는 등 백엔드 전반에 공통적으로 적용되는 패키지, 파일을 둔다. 예를 들면, 예외 처리, 공용 엔티티, cors 방지를 한 웹 설정 등

### 의존

`도메인 패키지`는 `공용 패키지`에 의존할 수 있다.

## `도메인 계층`

### 의존

`도메인 계층`에 있는 파일들은 **절대** `표현 계층`, `응용 계층`, `인프라 계층`에 의존하지 않는다.

`도메인 계층`에서 도메인은 엔티티와 합쳐진 형태이므로, 엔티티 관련 라이브러리에 의존할 수 있다. 예를 들면, Jakarta Persistence 인터페이스, Spring Data JPA 라이브러리 등

`도메인 계층`은 `공용 패키지`에 의존할 수 있다.


## `인프라 계층`

### 의존

`인프라 계층`은 **절대** `표현 계층`, `응용 계층`에 의존하지 않는다.

## `응용 계층`

### 의존

`응용 계층`은 **절대** `표현 계층`, `인프라 계층`에 의존하지 않는다.

## `표현 계층`

### 의존

`표현 계층`은 **절대** `도메인 계층`, `인프라 계층`에 의존하지 않는다.
`presentation`이 `domain`을 `import`하는 것은 허용 (DTO와 엔티티를 변환하기 때문)

## 엔티티와 DTO

엔티티는 **절대** HTTP 응답으로 직접 반환하지 않음.
`presentation`의 DTO로 변환해서 반환
`application`은 DTO를 **절대** 모르고, 엔티티를 받아서 엔티티를 반환

# 엔티티

- `@Setter`를 **절대** 쓰지 않음. 생성은 `private` 생성자 + `@Builder`, 변경은 `public` 메서드
- 자연키(`@NaturalId`)를 밖으로 노출하는 식별자로 쓰고, DB `id`는 API 경로나 참조에 쓰지 않음
- enum은 `@Enumerated(EnumType.STRING)`, 문자열은 `@Column(length = ...)` 명시
- 필드 검증은 엔티티의 `public static void validateXxx`에 두고, 생성자와 변경 메서드에서 **반드시** 호출. 먼저 검증하고 통과하면 대입
- 값을 바꾸는 규칙은 서비스가 아니라 엔티티 메서드에 둠
- 테이블 이름은 `{domain}_tb`, 유니크 제약 이름은 `uk_{domain}_{컬럼}`

# Repository

- 인터페이스는 `domain`, 구현은 `infrastructure`의 Adapter
- 반환 타입에 Spring Data 타입(`Page` 등)을 쓰지 않음
- `DomainEntityNotFoundException`은 Adapter의 `getByXxx`에서만 던짐
- 자연키 조회는 Hibernate `byNaturalId` 캐시 조회를 씀

# API

- 리소스 이름은 복수형, 경로 식별자는 자연키 (`/products/{number}`)
- 응답은 **반드시** `ResponseEntity<DTO>`
- Response에 비밀번호 같은 민감한 값을 **절대** 두지 않음

# 서비스

- `@Validated`, `@RequiredArgsConstructor`, `@Service`
- 모든 `public` 메서드에 `@Transactional`, 조회만 하면 `readOnly = true`
- 서비스에서 `Optional`을 풀어 예외를 던지지 않고 `getByXxx`를 호출

# 검증과 예외

- 검증 실패는 모두 `400`, `C002`. 같은 규칙의 문구는 DTO와 엔티티에서 같은 한글 문장으로 쓰고 마침표로 끝냄
- 에러 코드는 도메인별이 아니라 종류별 공용 코드. `C`는 클라이언트(`WARN`), `S`는 서버(`ERROR`) 오류
- 새 업무 예외는 `ErrorCode`에 값 추가, `BusinessException` 상속 클래스 작성, `GlobalExceptionHandler`는 고치지 않음
- 핸들러에 없는 예외는 전부 500

# 프로필 설정

- 프로필은 `local`, `test`, `stage`, `prod`
- 서버 프로필의 DB 접속 정보는 **반드시** 환경변수로 받고 yaml에 값을 **절대** 쓰지 않음
- `prod`는 `ddl-auto`를 `validate`로 유지하고 Swagger UI를 끔
- actuator는 별도 포트 8081에서 `prometheus`만 노출 (`rules/infra.md`)
