---
updated: 2026-10-22
---

# 백엔드 구조와 의존 방향

- `backend/spring-core-api`의 패키지 구조와 계층 사이의 의존 방향

## 패키지

- 기본 패키지는 `io.github.lsgeun.ecommerce`

- 기본 패키지의 바로 아래에 도메인(`product`, `user` 등)마다 하위 패키지를 하나씩 두고, 그 하위 패키지 안에 계층 패키지 4개(`presentation`, `application`, `domain`,  `infrastructure`)만 둔다
- `presentation`는 표현 계층, `application`는 어플리케이션 계층, 서비스 계층, `domain`는 도메인 계층,  `infrastructure`는 인프라스트럭처 계층이다.

- 기본 패키지의 바로 아래에 공용으로 쓰이는 패키지 `global`를 둔다.

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

> [!info] 역할 부분을 명확히 이해하고 다시 적어봐야 할 듯

| 계층             | 담긴 것                                                | 역할                                                    |
| ---------------- | ------------------------------------------------------ | ------------------------------------------------------- |
| `presentation`   | RestController, Dto, DtoMapper, 등                         | HTTP 입출력, 형식 검증, DTO와 엔티티 변환               |
| `application`    | SimpleService, 등                                          | 유스케이스 진행, 트랜잭션 경계, 저장된 상태에 의존하는 규칙 |
| `domain`         | 엔티티, 상태 enum, Repository 인터페이스, 등               | 데이터와 불변식, 상태 변경 메서드                       |
| `infrastructure` | RepositoryAdapter, JpaRepository, JpaCustomRepository, 등 | domain의 Repository 인터페이스를 JPA로 구현, 서비스에 사용되는 여러 인프라 구현             |

> [!question] domain에 상태 enum을 포함한, 돈, 이메일 등의 객체는 에그리거트인가?
> 복잡하고 유연하게 개발하려면 에그리거트를 알아야 할 거 같다.

# 의존 방향

```
presentation -> application -> domain <- infrastructure
```

화살표는 `import`하는 방향

`application`은 **반드시** `domain`의 Repository 인터페이스만 사용하고, `infrastructure`의 클래스를 **절대** `import`하지 않음

`domain`은 **절대** `presentation`, `application`, `infrastructure`를 `import`하지 않음
`domain`에서 JPA 애노테이션은 허용 (엔티티가 도메인 모델을 겸함). Spring MVC 애노테이션은 허용하지 않음

`infrastructure`는 **절대** `presentation`, `application`을 `import`하지 않음

`presentation`은 **절대** Repository를 직접 호출하지 않고, `application`의 SimpleService를 거침
`presentation`이 `domain`을 `import`하는 것은 허용 (DTO와 엔티티를 변환하기 때문)

## 엔티티와 DTO

> [!info] 도메인과 엔티티가 결합되었다는 걸 언급해야 할 듯

엔티티는 **절대** HTTP 응답으로 직접 반환하지 않음.
`presentation`의 DTO로 변환해서 반환
`application`은 DTO를 **절대** 모르고, 엔티티를 받아서 엔티티를 반환
