
# 프로젝트 디렉터리

이 프로젝트는 모노리포(Monorepo) 구조

## `backend/`

백엔드 프로젝트 폴더가 담기는 백엔드 폴더

- `spring-core-api/`
    - Spring Boot 백엔드
    - 핵심 비즈니스 로직을 담당하는 API 서비스
- `nginx/`
    - `default.conf`는 백엔드 서비스가 구동되는 서버의 Reverse Proxy 및 라우팅을 담당
- `promtail/`
    - 서버의 컨테이너 로그를 수집하여 로컬의 Loki로 전송하는 Promtail 설정 파일
- `docker-compose.yaml`
    - 하나의 서버에서 실행되는 mysql, backend, nginx, promtail 컨테이너를 정의

## `deploy/`

배포에 사용되는 폴더

- `aws-codedeploy/`
    - 서비스가 AWS 서버에 배포되고난 이후에 실행되는 AWS CodeDeploy 스크립트

## `monitoring/`

모니터링과 부하 테스트 폴더

부하 테스트를 할 때 모니터링을 할 수 있어야 성능이나 문제 상황 등을 측정할 수 있음

- `k6/`
    - 백엔드 API의 임계치 측정 및 부하 테스트를 위한 k6 시나리오 스크립트
- `prometheus/`
    - Spring Boot 백엔드 애플리케이션의 메트릭 수집 설정
- `loki/`
    - 서버의 Promtail이 전송한 로그를 수집 및 저장
- `grafana/`
    - 메트릭 및 로그 시각화를 위한 대시보드 환경
- `docker-compose.yaml`
    - 하나의 서버에서 실행되는 prometheus, loki, grafana 컨테이너를 정의

# **절대** 읽거나 수정하거나 삭제하면 안되는 폴더, 파일

- 빌드 결과물 폴더 (`**/bin/`, `**/out/`, `**/build/` 등)
