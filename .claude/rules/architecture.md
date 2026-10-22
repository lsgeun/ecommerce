---
updated: 2026-10-22
---

# 백엔드 구조와 의존 방향

- `backend/spring-core-api`의 패키지 구조와 계층 사이의 의존 방향
- 각 클래스를 쓰는 방법은 `backend-convention` 스킬을 읽는다

# 패키지

- 기본 패키지는 `io.github.lsgeun.ecommerce`
- 도메인(`product`, `user`)마다 패키지를 하나씩 두고, 그 안에 계층 패키지 4개를 둔다
- 도메인 패키지와 나란히 있는 공용 패키지는 `global`뿐이고, 지금은 `global/exception`만 있다

```
ecommerce/
    product/
        presentation/
        application/
        domain/
        infrastructure/
    user/        (product와 같은 모양)
    global/
        exception/
```

# 계층

| 계층             | 담긴 것                                                | 역할                                                    |
| ---------------- | ------------------------------------------------------ | ------------------------------------------------------- |
| `presentation`   | RestController, Dto, DtoMapper                         | HTTP 입출력, 형식 검증, DTO와 엔티티 변환               |
| `application`    | SimpleService                                          | 유스케이스 진행, 트랜잭션 경계, 저장된 상태에 의존하는 규칙 |
| `domain`         | 엔티티, 상태 enum, Repository 인터페이스               | 데이터와 불변식, 상태 변경 메서드                       |
| `infrastructure` | RepositoryAdapter, JpaRepository, JpaCustomRepository | domain의 Repository 인터페이스를 JPA로 구현             |

# 의존 방향

```
presentation -> application -> domain <- infrastructure
```

- 화살표는 `import`하는 방향
- `application`은 **반드시** `domain`의 Repository 인터페이스만 사용하고, `infrastructure`의 클래스를 **절대** `import`하지 않음
- `domain`은 **절대** `presentation`, `application`, `infrastructure`를 `import`하지 않음
- `infrastructure`는 **절대** `presentation`, `application`을 `import`하지 않음
- `presentation`은 **절대** Repository를 직접 호출하지 않고, `application`의 SimpleService를 거침
- `presentation`이 `domain`을 `import`하는 것은 허용 (DTO와 엔티티를 변환하기 때문)
- 모든 계층은 `global/exception`을 `import`할 수 있음
- `domain`에서 JPA 애노테이션은 허용 (엔티티가 도메인 모델을 겸함). Spring MVC 애노테이션은 허용하지 않음

## 엔티티와 DTO

- 엔티티는 **절대** HTTP 응답으로 직접 반환하지 않음. `presentation`의 DTO로 변환해서 반환
- `application`은 DTO를 **절대** 모르고, 엔티티를 받아서 엔티티를 반환

## 아직 정하지 않은 것

> 이해한 뒤에 정할 예정이라 적지 않음. 에이전트는 이 주제에 규칙이 있다고 가정하지 않음

- 도메인 사이의 참조 방식 (Aggregate 경계)
- 값 객체(`Money`, `Quantity` 등) 도입
- ArchUnit 의존 방향 테스트 (의존성은 `build.gradle`에 있지만 규칙 테스트는 아직 없음)
