---
updated: 2026-10-22
---

- `backend/spring-core-api/src/main/resources/`의 프로필별 설정 규칙
- 구체적인 값은 yaml 파일이 기준이므로 이 문서에 복사하지 않고, 프로필 사이의 **차이**만 적음

# 파일

- `application.yaml`: 모든 프로필 공통. 기본 프로필을 `local`로 지정 (`spring.profiles.default`)
- `application-{프로필}.yaml`: `local`, `test`, `stage`, `prod`

# 프로필별 차이

| 항목                | `local`          | `test`            | `stage`                 | `prod`                  |
| ------------------- | ---------------- | ----------------- | ----------------------- | ----------------------- |
| 쓰임                | 로컬 개발 실행   | 테스트            | 서버(EC2)에 배포되는 환경 | 정식 운영용 (배포 구성은 `stage`를 지정) |
| DB                  | H2 메모리        | H2 메모리         | MySQL (환경변수)         | MySQL (환경변수)         |
| `ddl-auto`          | `update`         | `create-drop`     | `update`                | `validate`              |
| H2 콘솔             | 켜짐 (`/h2-console`) | 설정 없음     | 꺼짐                    | 꺼짐                    |
| OSIV (`open-in-view`) | 설정 없음 (기본값 켜짐) | 설정 없음 (기본값 켜짐) | 꺼짐          | 꺼짐                    |
| Swagger UI          | 켜짐             | 설정 없음 (springdoc 기본값) | 켜짐         | 꺼짐                    |
| SQL 로그            | 출력             | 출력              | 끔                      | 끔                      |
| actuator            | 설정 없음        | 설정 없음         | 별도 포트 8081, `prometheus`만 | 별도 포트 8081, `prometheus`만 |

# 규칙

- 서버 프로필(`stage`, `prod`)의 DB 접속 정보는 **반드시** 환경변수(`MYSQL_HOST`, `MYSQL_USER`, `MYSQL_PASSWORD`)로 받음. 값을 yaml에 **절대** 쓰지 않음
- `prod`에서 `ddl-auto`는 **절대** `update`, `create`, `create-drop`으로 바꾸지 않음. 스키마가 바뀌는 사고를 막기 위함
- `prod`에서 Swagger UI와 api-docs는 **반드시** 꺼 둠
- actuator는 앱 포트(8080)가 아니라 **별도 포트 8081**에서 `prometheus` 엔드포인트만 노출. 그 외 엔드포인트(`health`, `info`)는 `access: none`
- 8081 포트는 컨테이너 밖으로 `127.0.0.1`에만 공개하고, nginx는 `/actuator`를 404로 막음 (`rules/infra.md`)
- 슬라이스, 통합, e2e 테스트는 `@ActiveProfiles("test")`로 `test` 프로필을 씀 (`src/test/.../support/annotation`의 테스트 애노테이션에 포함)
- Docker 이미지의 기본 프로필은 `stage` (`Dockerfile`의 `SPRING_PROFILES_ACTIVE`)

# 새 설정을 추가할 때

1. 모든 환경에 같은 값이면 `application.yaml`
2. 환경마다 다르면 필요한 프로필 파일에만 추가. 빠뜨린 프로필은 기본값으로 동작하므로, 기본값이 안전한지 확인
3. 비밀 값이면 환경변수(`${이름}`)로 받고, 서버에 넣는 방법(`backend/docker-compose.yaml`의 `environment`, GitHub Secrets)도 함께 확인 ([rules/infra.md](../../../rules/infra.md))

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- `stage`의 `ddl-auto: update`를 `validate`와 마이그레이션 도구로 바꾸는 방식
- `local`, `test`에서도 OSIV를 끄고 맞출지 여부
- `prod` 프로필을 실제로 배포에 쓰는 시점
