---
updated: 2026-10-06
---

# 프로젝트 디렉터리

이 프로젝트는 모노리포(Monorepo) 구조

제목의 깊이가 2이면서, 폴더 이름(예 `backend/`)으로 된 제목은 프로젝트 기준 루트 폴더를 의미
상위 제목이 폴더 이름을 가지고 있고, 폴더 이름을 가진 하위 제목은 그 상위 제목 폴더의 하위 폴더를 의미
폴더 이름을 가지고 있는 연속된 2개의 제목에 대해서, 제목 사이의 본문의 프로젝트 기준 경로는 상대적으로 위에 있는 제목의 경로

## `backend/`

백엔드 프로젝트 폴더가 담기는 백엔드 폴더

### `spring-core-api/`

Spring Boot 백엔드
핵심 비즈니스 로직을 담당하는 API 서비스

### `nginx/`

`default.conf`는 백엔드 서비스가 구동되는 AWS EC2 환경의 Reverse Proxy 및 라우팅을 담당하는 Nginx 설정 파일

배포 zip의 `nginx/`로 복사되고, `backend/docker-compose.yaml`에 정의된 Nginx 컨테이너와 마운트되어 적용

### `promtail/`

AWS EC2 인스턴스에서 발생하는 컨테이너 로그를 수집하여 로컬의 Loki로 전송하는 Promtail 설정 파일

배포 zip의 `promtail/`로 복사되고, `backend/docker-compose.yaml`에 정의된 Promtail 컨테이너와 마운트되어 적용

### `docker-compose.yaml`

AWS EC2 인스턴스 서버에서 실행되는 컨테이너(mysql-db, backend, nginx, promtail)를 정의

배포 zip의 루트로 복사됨

## `deploy/`

배포에 사용되는 폴더

### `aws-codedeploy/`

서비스가 AWS EC2 인스턴스 서버로 배포될 때 실행되는 CodeDeploy 스크립트.

## `monitoring/`

로컬에서만 쓰는 모니터링과 부하 테스트 폴더. 이 폴더의 `docker-compose.yaml`로 실행

`prometheus/`: Spring Boot 백엔드 애플리케이션의 메트릭 수집 설정
`loki/`: 서버의 Promtail이 전송한 로그를 수집 및 저장
`grafana/`: 메트릭 및 로그 시각화를 위한 대시보드 환경
`k6/`: 백엔드 API의 임계치 측정 및 부하 테스트를 위한 k6 시나리오 스크립트

# **절대** 읽거나 수정하거나 삭제하면 안되는 폴더, 파일

- 빌드 결과물 폴더 (`**/bin/`, `**/out/`, `**/build/` 등)
