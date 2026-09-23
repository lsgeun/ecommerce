---
paths:
  - "backend/**/src/test/**/{integration, product, user}/**/*.java"
---

# 테스트 유형별 어노테이션 규칙

- 테스트 대상과 실행 범위에 따라 아래 3가지 커스텀 어노테이션 중 하나를 테스트 클래스 상단에 **반드시** 붙인다.
- 필요한 경우 테스트 클래스 상단에 표준 Spring/JUnit 어노테이션(`@ExtendWith`, `@WebMvcTest`, `@DataJpaTest` 등)을 자유롭게 추가하여 확장한다.
- 아래의 커스텀 어노테이션 3개는 `backend/spring-core-api/src/test/java/io/github/lsgeun/ecommerce/support/annotation/`에 정의되어 있다.

- **`@UnitTest`**: Spring 컨텍스트 없이 수행되는 순수 Java 단위 테스트
  - 내부 설정: `@Tag("unit")`만 포함
- **`@SliceTest`**: 특정 레이어만 격리하여 수행하는 테스트
  - 내부 설정: `@Tag("slice")`, `@ActiveProfiles("test")`
  - 검증 대상에 따라 `@WebMvcTest`, `@DataJpaTest` 등을 추가로 명시한다.
- **`@IntegrationTest`**: 모든 빈을 구동하여 수행하는 전체 통합 테스트
  - 내부 설정: `@Tag("integration")`, `@ActiveProfiles("test")`, `@SpringBootTest`

> 참고: **빌드 파이프라인 및 테스트 실행 순서 (Fail-Fast Rule)**
> `backend/spring-core-api/build.gradle` 설정에 의해 테스트 실행 순서는 **`UnitTest` ➔ `SliceTest` ➔ `IntegrationTest`** 로 강제된다.
> 빠른 실행 속도를 가진 테스트를 먼저 수행하여, 피드백을 신속하게 받고 빌드 오버헤드를 줄이기 위함이다.
