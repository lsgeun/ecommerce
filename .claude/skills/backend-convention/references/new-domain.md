---
updated: 2026-10-22
---

- 새 도메인(`order`처럼 `product`, `user`와 나란한 업무 단위)을 추가할 때의 패키지, 이름, 순서

# 패키지

- 기본 패키지 `io.github.lsgeun.ecommerce` 아래에 `{domain}` 패키지를 만들고, 그 안에 `presentation`, `application`, `domain`, `infrastructure` 4개를 **반드시** 모두 만듦
- 도메인 이름은 영문 소문자 단수형 (`product`, `user`)
- 다른 도메인의 클래스를 **절대** `import`하지 않음 (참조 방식은 정하지 않음)

# 클래스 이름

`{Domain}`은 도메인 이름의 파스칼 케이스 (`Product`)

| 계층             | 클래스                                   | 이름                                     | 예시                                  |
| ---------------- | ---------------------------------------- | ---------------------------------------- | ------------------------------------- |
| `presentation`   | RestController                           | `{Domain}RestController`                 | `ProductRestController`               |
| `presentation`   | DTO 묶음                                 | `{Domain}Dto`                            | `ProductDto`                          |
| `presentation`   | MapStruct 매퍼                           | `{Domain}DtoMapper`                      | `ProductDtoMapper`                    |
| `application`    | 서비스                                   | `{Domain}SimpleService`                  | `ProductSimpleService`                |
| `domain`         | 엔티티                                   | `{Domain}`                               | `Product`                             |
| `domain`         | 상태 enum                                | `{Domain}Status`                         | `ProductStatus`                       |
| `domain`         | Repository 인터페이스                    | `{Domain}Repository`                     | `ProductRepository`                   |
| `infrastructure` | Repository 구현                          | `{Domain}RepositoryAdapter`              | `ProductRepositoryAdapter`            |
| `infrastructure` | Spring Data JPA 인터페이스               | `{Domain}JpaRepository`                  | `ProductJpaRepository`                |
| `infrastructure` | 직접 구현하는 조회 인터페이스와 구현     | `{Domain}JpaCustomRepository`, `...Impl` | `ProductJpaCustomRepositoryImpl`      |

- `{Domain}JpaCustomRepository`는 naturalId 캐시 조회처럼 Spring Data 메서드로 만들 수 없는 조회가 있을 때만 만듦

# 만드는 순서

1. `domain`: 엔티티, 상태 enum, Repository 인터페이스 ([domain](domain.md))
2. `infrastructure`: JpaRepository, RepositoryAdapter ([infrastructure](infrastructure.md))
3. `application`: SimpleService ([application](application.md))
4. `presentation`: Dto, DtoMapper, RestController ([presentation](presentation.md))
5. 테스트: `test-code` 스킬을 읽는다 (단위, 슬라이스, 통합 순서)

# 체크리스트

- 4계층 패키지가 모두 있는가
- 엔티티 테이블 이름이 `{domain}_tb`이고, 유니크 제약 이름이 `uk_{domain}_{컬럼}`인가 ([domain](domain.md))
- 엔티티를 생성하는 곳에서 `validateXxx`가 호출되는가 ([validation](validation.md))
- 없을 때 던지는 예외가 `DomainEntityNotFoundException`, 중복일 때 던지는 예외가 `DomainEntityAlreadyExistsException`인가 ([exception](exception.md))
- 응답에 엔티티를 직접 쓰지 않고 DTO를 쓰는가
- `./gradlew compileJava compileTestJava`가 통과하는가 (`rules/how-to-run.md`)
