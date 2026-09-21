## 📂 프로젝트 구조

본 프로젝트는 모노리포 구조로 관리되고 있습니다. 
자세한 모듈별 역할 및 디렉터리 구조는 [project-structure.md](docs/project-structure.md) 문서에서 확인하실 수 있습니다.

* **`backend/`**: Spring Boot API 서비스
* **`deploy/`**: AWS CodeDeploy 자동 배포 스크립트
* **`gateway/`**: Nginx Reverse Proxy 설정
* **`monitoring/`**: Prometheus, Promtail, Loki, Grafana 모니터링
* **`scripts/`**: k6 부하 테스트 스크립트
