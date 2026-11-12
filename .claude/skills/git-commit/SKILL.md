---
name: git-commit
description: |
  사용자가 커밋 요청을 할 때 이 스킬을 사용
  의미 있는 작업 단위로 나누고 Conventional Commits 규칙(type, scope, description, body)에 맞춰 한국어 커밋 메시지를 작성
---

바로 커밋하지 말고, 변경사항을 어떤 식으로 나누고, 어떤 식으로 메시지를 작성할 것인지 사용자에게 알려야 함.

# 작업 단위별로 커밋

필요하다면 청크 단위로도 나누고, 의미있는 작업 단위별로 커밋 메시지를 작성
되도록 하나의 작업 단위는 하나의 커밋으로 처리

# 커밋 컨벤션

[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)를 따름

아래 코드 블록은 커밋 메시지 형식

```
[type]([scope]): [description]

[body]
```

- `type`
    - 필수 사항
    - 변경 사항의 종류
    - **반드시** `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `ci`, `build`, `revert` 중 하나
- `scope`
    - 선택 사항
    - 변경 사항이 영향을 미치는 범위
    - `scope`을 작성할 때는 **반드시** 소괄호로 감쌀 것
    - **반드시** 소문자 영어 케밥 스타일로 작성할 것
- `description`
    - 필수 사항
    - 변경 사항에 대한 짧은 요약
- `body`
    - 선택 사항
    - 변경 사항에 대해 알아둬야 할, 중요하고, 구체적인 사실을 정리하여 나열
    - 변경된 코드의 길이나 줄 수, 크기 등 물리적인 수치보다는 변경된 코드가 의미하는 바가 중요한지 따져서, 중요한 것 위주로 body에 남길 것
    - 장황하게 작성하지 말고, 핵심 내용 위주로 요약하여 변경사항을 빠르게 파악하기 쉽게 작성

- `description`, `body`는 이해하기 쉽게 요약하여 한글을 활용하여 작성
