---
name: update-project-directory-docs
description: >
   프로젝트 구조 문서(docs/project-directory.md, README.md의 프로젝트 구조 섹션)를 실제 디렉터리 구조와 대조하여 검증하고 최신 상태로 갱신한다.
   
---

# 프로젝트 구조 문서 업데이트

`docs/project-directory.md`는 프로젝트 구조에 대한 단일 출처(source of truth)이며 `.claude/CLAUDE.md`에서 `@import`로 항상 시스템 프롬프트에 로드된다. `README.md`는 이 문서를 요약해서 사람이 보는 진입점 역할을 한다. 두 문서가 실제 코드와 어긋나면 안 된다.

## 절차

1. **실제 프로젝트 구조 스캔 (추측 금지)**
   - 저장소 루트 기준 최상위 디렉터리 구조를 확인한다 (`ls`, `find . -maxdepth 2 -type d` 등).
   - `.gitignore`에 등록된 파일/디렉터리, `.git`, `node_modules`, `build/`, `dist/`, `.DS_Store` 등을 반드시 제외하고 탐색한다.

2. `**기존 문서와 대조 분석**
   - `docs/project-directory.md`를 읽고 언급된 모든 경로(`backend/...`, `gateway/...` 등)의 실제 존재 여부를 검증한다.
   - 다음 불일치 항목을 추출한다:
     - **신규**: 프로젝트에 추가되었으나 문서에 없는 최상위/주요 디렉터리 및 핵심 설정 파일
     - **삭제**: 문서에는 있으나 실제로는 존재하지 않는 경로
     - **변경**: 이름 변경, 이동, 또는 역할 변화(예: Docker Compose 구성 변경, 스크립트 경로 이동 등)
     - 코드 파일 탐색만으로 역할을 알 수 없는 디렉터리나 파일은 임의로 추측해 적지 말고 **반드시 사용자에게 용도를 질의**한다.

3. **`docs/project-directory.md` 갱신**
   - 불일치가 확인되면 문서를 수정한다.
   - 기존 문서의 톤(한국어, 섹션별 `###` 헤딩, 상대 경로 기준)과 포맷팅 방식을 철저히 유지한다.

4. **`README.md` 동기화 및 교차 검증**
   - `docs/project-directory.md`의 최상위 구조가 수정되었다면, `README.md`의 구조 요약 불릿 목록도 동일하게 수정한다.
   - `docs/project-directory.md`와 `README.md` 간 목록 항목이 1:1로 일치하는지 교차 확인하고, 두 문서 내용이 모순되지 않도록 한다.

5. **결과 보고**
   - 변경된 내역을 **[추가] / [삭제] / [수정]** 범주로 나누어 사용자에게 명확히 보고한다.

## 사용자 승인

`docs/project-directory.md`에 없던 새로운 섹션 구성이나 포맷을 추가, 수정, 삭제할 경우, 사용자에게 어떤 것을 어떤 이유로 변경할지 명확하게 알리고, 승인을 요구한다.

## 하지 않을 것

다음 불릿 리스트는 `docs/project-directory.md`에 반영하지 않는다.

- 프로젝트 구조와 무관한 내용(설치 방법, 라이선스, OS 전용 파일 등)
- `.gitignore` 대상 파일(빌드 결과물, 환경변수 `.env` 파일 등)
- '.'으로 시작하는 숨겨진 폴더의 하위 파일, 폴더와 숨겨진 파일(`.git/`, `.claude/` `.gitignore` 등)
- 빌드 결과물 (`/bin`, `/out`, `/build` 등)
- 설치된 모듈 (`node_modules` 등)
