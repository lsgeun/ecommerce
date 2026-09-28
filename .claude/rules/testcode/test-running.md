---
paths:
  - "backend/**/src/test/**/{integration, product, user}/**/*.java"
---

# 테스트 실행 규칙

## 빌드 파이프라인 및 테스트 실행 순서 (Fail-Fast Rule)

- `backend/spring-core-api/build.gradle` 설정에 의해 `test` 태스크는 비활성화되어 있고, 대신 계층별 태스크(`unitTest`, `sliceTest`, `integrationTest`, `e2eTest`)로 대체된다.
- 테스트 실행 순서는 **`unitTest` ➔ `sliceTest` ➔ `integrationTest` ➔ `e2eTest`** 로 강제된다.
- 빠른 실행 속도를 가진 테스트를 먼저 수행하여, 피드백을 신속하게 받고 빌드 오버헤드를 줄이기 위함이다.

## 실행 방법

`backend/spring-core-api` 디렉터리에서 아래 명령을 실행한다.

- **단위 테스트만 실행:** `./gradlew unitTest`
- **슬라이스 테스트만 실행:** `./gradlew sliceTest`
- **통합 테스트만 실행:** `./gradlew integrationTest`
- **E2E 테스트만 실행:** `./gradlew e2eTest`
- **전체 실행 (단위 ➔ 슬라이스 ➔ 통합 ➔ E2E 순):** `./gradlew testAll`

### 특정 클래스/메서드만 실행

- 클래스 지정: `./gradlew unitTest --tests "io.github.lsgeun.ecommerce.product.application.ProductSimpleServiceUnitTest"`
- 메서드 지정: `./gradlew unitTest --tests "io.github.lsgeun.ecommerce.product.application.ProductSimpleServiceUnitTest.getProduct_존재하는_번호_조회_성공"`
- 어떤 태스크(`unitTest`/`sliceTest`/`integrationTest`/`e2eTest`)를 쓸지는 테스트 클래스에 붙은 어노테이션(`@UnitTest`/`@SliceTest`/`@IntegrationTest`)에 맞춰 선택한다.

### 결과가 캐시되어 반영되지 않을 때

- Gradle이 소스 변경 없음으로 판단해 태스크를 건너뛰면(`UP-TO-DATE`, `SKIPPED`), `--rerun` 옵션을 붙여 강제로 재실행한다.
  - 예: `./gradlew unitTest --tests "..." --rerun`
