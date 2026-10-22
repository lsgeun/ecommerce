---
updated: 2026-10-22
---

# 인프라 뼈대

- 서버가 어떻게 구성되고, 코드가 어떤 경로로 서버에 배포되는지 큰 그림
- 구체적인 설정 값(포트, 보관 기간, 힙 크기 등)은 적지 않음. 값이 필요하면 아래 표의 파일을 읽는다

# 한 줄 요약

1. 서버는 바깥에서 들어오는 문을 모두 닫고 tailscale 안에서만 열려 있음
2. 배포는 push가 아니라 서버가 당겨오는 pull 방식 (CodeDeploy agent가 폴링하고, 이미지는 서버가 Docker Hub에서 pull). 그래서 인바운드가 필요 없음
3. 저장과 시각화는 로컬(Prometheus, Loki, Grafana)에서 하고, 서버에는 수집기(promtail, actuator)만 둠

# 서버 구성

- AWS EC2 인스턴스 1대에서 `backend/docker-compose.yaml`로 컨테이너 4개를 실행

| 컨테이너                | 역할                                        | 설정 파일                       |
| ----------------------- | ------------------------------------------- | ------------------------------- |
| `ecommerce-nginx`       | tailscale IP의 80, 443만 받는 reverse proxy | `backend/nginx/default.conf`    |
| `ecommerce-backend-stage` | Spring Boot (`stage` 프로필)               | `backend/spring-core-api/`      |
| `mysql-db`              | MySQL. 데이터는 호스트 볼륨에 보관          | `backend/docker-compose.yaml`   |
| `ecommerce-promtail`    | 컨테이너 로그를 수집해서 로컬 Loki로 전송   | `backend/promtail/`             |

- 요청 경로: 클라이언트 -> nginx -> backend -> mysql-db
- 백엔드 앱 포트와 MySQL 포트는 컨테이너 밖으로 열리지 않거나 `127.0.0.1`에만 열림. 바깥에서 접근 가능한 것은 nginx뿐
- `/actuator` 경로는 nginx가 막음. 메트릭용 management 포트는 `127.0.0.1`에만 공개되고 ssh 터널링으로 받음

# 배포 경로

```
push -> GitHub Actions(build-test) -> GitHub Actions(deploy) -> Docker Hub, S3 -> CodeDeploy -> EC2
```

- 워크플로우 파일은 `.github/workflows/backend-ci-cd-deploy-to-ec2.yaml`

## build-test

- 모든 브랜치의 push에서 실행. 같은 브랜치에 연속으로 push하면 이전 실행은 취소
- `./gradlew clean build`를 실행하므로 테스트 4종이 통과해야 함
- 변경된 파일이 `**.md`, `.claude/**`, `.obsidian/**`뿐이면 워크플로우를 실행하지 않음
- 실패하면 Discord 웹훅으로 알림

## deploy

- 실행 조건: build-test 성공 **그리고** (`main` 브랜치 **또는** 커밋 메시지에 `[deploy]` 포함)
- 순서: jar 다운로드 -> Docker 이미지 빌드 후 Docker Hub push(태그는 항상 `latest`) -> 배포 zip 생성 -> S3 업로드 -> CodeDeploy 배포 생성
- 배포 zip에는 `appspec.yml`, `start.sh`, `stop.sh`, `nginx/`, `promtail/`, `docker-compose.yaml`이 들어감 (`backend/`의 `nginx/`, `promtail/`, `docker-compose.yaml`을 복사)
- 즉 `backend/docker-compose.yaml`, nginx 설정, promtail 설정을 고치고 배포하면 서버에 반영됨

## 서버에서 실행되는 것

| 시점                      | 스크립트                          | 하는 일                                                |
| ------------------------- | --------------------------------- | ------------------------------------------------------ |
| `AfterInstall`            | `deploy/aws-codedeploy/stop.sh`   | `docker compose down`                                  |
| `ApplicationStart`        | `deploy/aws-codedeploy/start.sh`  | `docker compose pull`, `up -d`, `image prune`          |

- 서버의 배포 로그는 `/home/ubuntu/app/deploy.log`

# 건드릴 때 주의

- 시크릿 값(`DOCKERHUB_*`, `AWS_*`, `MYSQL_*`, `DISCORD_WEBHOOK_URL`)은 레포에 없고, GitHub Secrets와 서버 환경에만 있음. 값을 추측하거나 레포에 쓰지 않음
- nginx를 `0.0.0.0`이나 공개 IP에 바인딩하도록 **절대** 바꾸지 않음 (보안 경계가 무너짐)
- `ports`에 `127.0.0.1:` 없이 포트를 추가하지 않음
- 이미지 태그는 항상 `latest`라서, 이전 버전으로 되돌리려면 이미지가 아니라 커밋을 되돌려 다시 배포

# 확인되지 않은 것

> 레포 코드에서 확인할 수 없어서 사실로 적지 않음

- ssh 터널링 명령 자체 (로컬에서 어떤 옵션으로 여는지)
- 로컬 모니터링의 Prometheus가 메트릭을 가져오는 정확한 경로
