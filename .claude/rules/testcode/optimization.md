---
paths:
  - "backend/**/src/test/**/{integration, product, user}/**/*.java"
---

# 유지보수 최적화

- **상태 검증 우선:** 최종 반환값이나 객체/DB의 내부 상태 변화(`assertThat`)를 최우선으로 검증하며, 단순 메서드 호출 여부를 확인하는 `verify()` 남용을 지양한다.
- **Helper/Fixture 활용:** `given` 절이 복잡해지지 않도록 객체 생성을 전담하는 테스트 전용 Helper/Fixture 메서드를 적극 활용한다.

# 실행 최적화

- **최소 데이터 준비:** 해당 테스트 케이스 검증에 필요한 필드/데이터만 준비하고, 결과와 무관한 부가 데이터는 만들지 않는다.
- **컨텍스트 재사용:** `@SliceTest`, `@IntegrationTest`에서 Spring 컨텍스트가 테스트마다 불필요하게 재생성되지 않도록, 동일한 조건의 테스트는 가능한 한 같은 설정(`@ActiveProfiles`, `@MockBean` 대상 등)을 공유한다.
- **느린 테스트 지양:** `Thread.sleep()` 등 실행 시간을 인위적으로 늘리는 대기 코드를 사용하지 않는다. 비동기/시간 의존 검증이 필요하면 `Awaitility` 같은 폴링 기반 도구를 사용하거나, `Clock` 추상화로 시간을 제어한다.
