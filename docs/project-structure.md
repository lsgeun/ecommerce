# 프로젝트 구조 (Project Structure)

이 프로젝트는 모노리포(Monorepo) 구조입니다.

> **참고:** 아래 기술된 모든 경로는 프로젝트 루트 디렉터리 기준의 상대 경로입니다.

### `backend/` 백엔드

`backend/spring-core-api/`: Spring Boot 기반의 메인 비즈니스 로직을 담당하는 핵심 API 서비스입니다.

### `deploy/` 배포

`deploy/aws-codedeploy/`: GitHub Actions 워크플로우(`.github/workflows/backend-ci-cd-deploy-to-ec2.yaml`)와 연동되어 AWS EC2 인스턴스로의 자동 배포를 수행하는 CodeDeploy 스크립트 모음입니다.

### `gateway/` 게이트웨이

`gateway/nginx/default.conf`: 백엔드 서비스가 구동되는 AWS EC2 환경의 Reverse Proxy 및 라우팅을 담당하는 Nginx 설정 파일입니다. 루트의 `docker-compose.yaml`에 정의된 Nginx 컨테이너와 마운트되어 적용됩니다.

### `monitoring/` 모니터링

모니터링은 운영 서버와 로컬로 분리되어 구성됩니다.

#### **운영 서버(AWS EC2)** (루트 `docker-compose.yaml`)

`monitoring/prometheus/`: Spring Boot 백엔드 애플리케이션의 실시간 메트릭 수집 설정
`monitoring/promtail/`: AWS EC2 인스턴스에서 발생하는 로그를 수집하여 로컬의 Loki로 전송

#### **로컬** (`monitoring/docker-compose.yaml`)

`monitoring/grafana/`: 메트릭 및 로그 시각화를 위한 대시보드 환경
`monitoring/loki/`: Promtail이 전송한 로그를 수집 및 저장

> 로컬 모니터링(Grafana, Loki) 및 운영 서버(AWS EC2) 모니터링(Promtail)의 설정 파일은 개발용 로컬 호스트를 바라보도록 루프백 주소(`127.0.0.1` / `localhost`) 기반 엔드포인트로 구성되어 있습니다.

### `scripts/` 스크립트

`scripts/k6/`: 백엔드 API의 임계치 측정 및 부하 테스트를 위한 k6 시나리오 스크립트 모음입니다.
