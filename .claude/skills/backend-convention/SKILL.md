---
updated: 2026-10-22
name: backend-convention
description: |-
    ecommerce 백엔드(`backend/spring-core-api`)의 코드 작성 규칙 문서. 새 도메인 추가, RestController, DTO, DtoMapper, SimpleService, 엔티티, Repository 인터페이스, RepositoryAdapter, JpaRepository, 예외와 ErrorCode, 검증, 프로필별 설정.
    위 클래스를 새로 만들거나 고치거나 삭제하기 전에 필요한 문서를 골라 읽는다. 테스트 코드는 `test-code` 스킬을 읽는다.
---

# 백엔드 컨벤션

- 계층 구조와 의존 방향은 `rules/architecture.md`, 실행 방법은 `rules/how-to-run.md`에 있고 항상 읽힘. 이 스킬은 클래스를 만들거나 고칠 때 읽음
- 각 문서는 이 프로젝트의 규칙과 `product` 도메인의 실제 코드를 기준으로 한 예시를 담음
- 규칙은 **코드에서 확인한 사실**만 적음. 코드와 문서가 다르면 코드를 먼저 확인하고 문서를 고침

| 대상                                                                            | 문서                                                  | 담긴 내용                                              |
| ------------------------------------------------------------------------------- | ----------------------------------------------------- | ------------------------------------------------------ |
| 새 도메인 추가 (패키지, 클래스 이름, 만드는 순서)                               | [new-domain](references/new-domain.md)                | 도메인 패키지 4개, 클래스 이름 규칙, 체크리스트        |
| RestController, Dto, DtoMapper                                                  | [presentation](references/presentation.md)            | API 경로, 응답, DTO 모양, MapStruct                    |
| SimpleService                                                                   | [application](references/application.md)              | 트랜잭션, 서비스 메서드, 중복 확인                     |
| 엔티티, 상태 enum, Repository 인터페이스, DB 이름                                | [domain](references/domain.md)                        | 엔티티 모양, `validateXxx`, 상태 변경 메서드, 테이블 이름 |
| RepositoryAdapter, JpaRepository, JpaCustomRepository                           | [infrastructure](references/infrastructure.md)        | 인터페이스 구현, `find`와 `get`, naturalId 캐시 조회   |
| 예외 클래스, ErrorCode, ErrorResponse, GlobalExceptionHandler                    | [exception](references/exception.md)                  | 예외 종류와 응답 코드, 새 예외 추가 순서               |
| 입력 검증 (DTO, @PathVariable, 엔티티)                                          | [validation](references/validation.md)                | 검증 3곳, 실패 응답, 중복되는 규칙                     |
| `application-*.yaml` 프로필별 설정                                              | [config](references/config.md)                        | 프로필별 DB, ddl-auto, OSIV, swagger, actuator         |

# 어떤 작업에 어떤 문서를 읽는가

| 작업                           | 읽는 순서                                                                              |
| ------------------------------ | -------------------------------------------------------------------------------------- |
| 새 도메인 추가                 | new-domain -> domain -> infrastructure -> application -> presentation -> `test-code` 스킬 |
| 기존 도메인에 API 추가         | presentation -> application -> (필요하면) domain                                       |
| 필드 추가, 규칙 변경           | domain -> validation -> presentation(DTO)                                              |
| 새 에러 응답 추가              | exception                                                                              |
| 설정 변경                      | config                                                                                 |

# 규칙을 적는 방식

- **반드시**, **절대**는 어기면 안 되는 규칙
- 값이 없을 때의 기본 동작은 규칙 옆에 적음
- 코드가 일관되지 않거나 정하지 않은 것은 각 문서의 `정하지 않은 것`에 모아 두고, 에이전트는 그 주제에 규칙이 있다고 가정하지 않음

# 정하지 않은 것 (전체)

> 이해한 뒤에 정할 예정이라 어떤 문서에도 규칙으로 적지 않음. 물어보지 않고 임의로 정하지 않음

- 도메인 사이의 참조 방식 (Aggregate 경계)
- 비밀번호 해시 저장과 응답에서 제외하는 방식 (지금 `User.password`는 평문 저장이고 응답에도 포함됨)
- 재고 동시성 제어 (낙관적 락, 재시도)
- 스키마 마이그레이션 도구 (Flyway 등)
- HTTP 의미 정리 (생성 시 201, 수정 경로에 식별자 포함 등)
- 검증 규칙의 단일 출처 (지금은 같은 규칙이 여러 곳에 있음, `validation` 참고)
- 서비스를 유스케이스 단위로 나누는 기준 (`SimpleService`가 커졌을 때)
