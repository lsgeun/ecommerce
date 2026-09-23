---
paths:
  - "backend/**/src/test/**/{integration, product, user}/**/*.java"
---

# 검증 범위

- 단순 Getter/Setter, DTO 기본 생성, Spring Framework 자체의 동작은 테스트 대상에서 제외한다.
- 비즈니스 예외 흐름, 경계값(Boundary value), 상태 변화(State change) 검증에 집중한다.
- **상태 검증 우선:** 최종 반환값이나 객체/DB의 내부 상태 변화(`assertThat`)를 최우선으로 검증하며, 단순 메서드 호출 여부를 확인하는 `verify()` 남용을 지양한다.
- **Helper/Fixture 활용:** `given` 절이 복잡해지지 않도록 객체 생성을 전담하는 테스트 전용 Helper/Fixture 메서드를 적극 활용한다.
