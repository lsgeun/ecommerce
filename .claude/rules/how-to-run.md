# 실행과 검증 원칙

- 실행, 컴파일, 빌드 명령은 `backend/spring-core-api`의 Gradle 설정(`build.gradle`)과 Gradle 태스크 목록을 읽어서 확인하고, 이 문서에 명령어를 중복해서 적지 않음

- 코드를 고친 뒤에는 무거운 작업(전체 빌드, 서버 실행)보다 가벼운 작업(컴파일, 관련 테스트)으로 먼저 오류를 확인
- 문제가 없다고 보고하려면 **반드시** 실제로 실행한 결과를 근거로 함

# 테스트

- 테스트는 계층(단위, 슬라이스, 통합, e2e)별로 나뉘고, 빠른 계층부터 실패하면 멈추는 순서로 실행
- 기본 `test` 태스크는 모든 계층을 그 순서대로 실행한 뒤, 계층 태그가 없는 테스트까지 실행
- 일부만 확인할 때는 변경한 코드와 관련된 계층과 클래스만 실행
- 클래스나 메서드를 지정하는 방법, 결과가 캐시될 때 재실행하는 방법은 `test-code` 스킬의 `references/test-running.md`를 읽는다

## 빌드 파이프라인 및 테스트 실행 순서 (Fail-Fast Rule)

- 빠른 실행 속도를 가진 테스트를 먼저 수행하여, 피드백을 신속하게 받고 빌드 오버헤드를 줄이기 위함이다.

## 실행 방법

### 특정 클래스/메서드만 실행

- 클래스 지정: `./gradlew unitTest --tests "io.github.lsgeun.ecommerce.product.application.ProductSimpleServiceUnitTest"`
- 메서드 지정: `./gradlew unitTest --tests "io.github.lsgeun.ecommerce.product.application.ProductSimpleServiceUnitTest.getProduct_존재하는_번호_조회_성공"`
- 어떤 태스크(`unitTest`/`sliceTest`/`integrationTest`/`e2eTest`)를 쓸지는 테스트 클래스에 붙은 어노테이션(`@UnitTest`/`@SliceTest`/`@IntegrationTest`)에 맞춰 선택한다.

### 결과가 캐시되어 반영되지 않을 때

- Gradle이 소스 변경 없음으로 판단해 태스크를 건너뛰면(`UP-TO-DATE`, `SKIPPED`), `--rerun` 옵션을 붙여 강제로 재실행한다.
  - 예: `./gradlew unitTest --tests "..." --rerun`

# 금지

- 테스트가 실패했는데 테스트를 비활성화하거나 삭제해서 통과시키지 않음
- 확인하려는 목적 없이 `clean build`처럼 오래 걸리는 명령을 반복 실행하지 않음
