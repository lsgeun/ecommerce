# 테스트 코드 위치 및 명명 규칙

## 파일명 규칙

### 단위 / 슬라이스 테스트

- `<검증 대상 클래스명>` + 어노테이션 접미사로 짓는다.
  - `@UnitTest` → `<클래스명>UnitTest.java`
  - `@SliceTest` → `<클래스명>SliceTest.java`

### 통합 테스트

- 목적 키워드는 아래 목록에서 선택하고, 새로운 검증 목적이 생기면 이 목록에 추가한다.
  - `Crud`: 생성/조회/수정/삭제 등 기본 기능 검증
  - `Scenario`: 여러 기능이 연계된 비즈니스 흐름 검증
  - `Query`: 복합 조회/조건 검증
  - `Concurrency`: 동시성/경쟁 상태 검증

#### 하나의 도메인에만 의존하는 경우

- 파일명 형식: `<도메인명>[구체적 설명]<목적 키워드>IntegrationTest.java`
- 하나의 도메인이라도 검증 목적(키워드)이 다르면 파일을 나누어 작성한다. `<도메인명>IntegrationTest.java` 하나로 몰아넣지 않는다.
- "구체적 설명"은 같은 키워드 안에서 파일을 구분하기 위해 자유롭게 붙인다.
- 예: `ProductCrudIntegrationTest.java`, `ProductStockScenarioIntegrationTest.java`

#### 여러 도메인에 걸친 경우

- 파일명 형식: `<비즈니스 유스케이스(흐름) 이름><목적 키워드>IntegrationTest.java`
- 관련 도메인명을 나열하지 않고, 검증 대상 비즈니스 유스케이스(흐름)의 이름을 드러낸다. (아래 디렉터리 규칙에 따라 `integration/` 최상위 패키지에 위치한다는 사실 자체가 특정 도메인에 속하지 않음을 이미 나타내므로, 도메인명 나열은 불필요한 중복이다.)
- 예: `CheckoutScenarioIntegrationTest.java`, `OrderPaymentScenarioIntegrationTest.java`

## 디렉터리 규칙

### 단위 / 슬라이스 테스트

- 패키지 경로는 검증 대상 클래스가 위치한 main 패키지 경로와 완전히 동일해야 한다.
  - 예: `main/.../product/application/ProductSimpleService.java` → `test/.../product/application/ProductSimpleServiceUnitTest.java`

### 통합 테스트

#### 하나의 도메인에만 의존하는 경우

- 해당 도메인 루트 패키지 바로 아래 `integration` 패키지에 위치시킨다.
- 예: `test/.../product/integration/ProductCrudIntegrationTest.java`

#### 여러 도메인에 걸친 경우

- 특정 도메인에 종속시키지 않고, 최상위 test 패키지 바로 아래 `integration` 패키지에 위치시킨다.
- 예: `test/.../integration/CheckoutScenarioIntegrationTest.java`
