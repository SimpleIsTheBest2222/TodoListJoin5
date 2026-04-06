# TodoListJoin5

Spring 없는 순수 Java 콘솔 기반 Todo 관리 애플리케이션입니다.
레이어드 아키텍처를 직접 구현하며 도메인 설계 및 의존성 흐름을 학습하는 것을 목표로 합니다.

---

## 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Java 17 |
| Build | Gradle (Groovy DSL) |
| Test | JUnit 5 |
| 외부 의존성 | 없음 (순수 Java) |

---

## 기능 명세

### 핵심 기능 (CRUD)

| 기능 | 설명 |
|------|------|
| 할 일 추가 | 내용, 우선순위 입력 후 저장 |
| 할 일 조회 | 전체 목록 테이블 형식 출력 |
| 할 일 수정 | 내용, 상태, 우선순위 변경 |
| 할 일 삭제 | ID 기반 삭제 |

### 세부 기능

| 기능 | 설명 |
|------|------|
| 상태 관리 | 각 항목은 시작 전, 진행 중 또는 완료 상태를 가집니다. |
| 날짜 및 우선순위 | 할 일 생성 시 날짜와 우선순위(숫자 0, 1, 2..)를 추가로 설정할 수 있습니다. |
| 검색 및 필터링 | 특정 키워드가 포함된 할 일을 찾거나, 완료된 항목만 따로 볼 수 있는 필터링 기능을 제공합니다. |
| 메시지 처리 | 성공 메시지, 비어있는 목록 처리 |

---

## 패키지 구조

```
src/main/java/com/todolist/
├── TodoListApplication.java            ← 진입점 (Scanner 소유, 흐름 제어, 예외 처리)
├── common/
│   ├── ErrorCode.java                  ← 예외 메시지 enum으로 중앙 관리
│   └── CustomException.java            ← 단일 커스텀 예외 클래스
├── domain/
│   ├── Todo.java                       ← 할 일 엔티티
│   └── TodoStatus.java                 ← 상태 enum (시작전/진행중/완료)
├── repository/
│   ├── TodoRepository.java             ← 인터페이스
│   └── InMemoryTodoRepository.java     ← 구현체 (ArrayList 기반)
├── service/
│   └── TodoService.java                ← 비즈니스 로직
└── view/
    ├── MenuInputView.java              ← 입력 + 유효성 검증 담당
    └── MenuViewOutputView.java         ← 출력 담당

src/test/java/com/todolist/
├── service/
│   └── TodoServiceTest.java
└── repository/
    └── InMemoryTodoRepositoryTest.java
```

---

## 도메인 설계

### TodoStatus.java

```java
package com.todolist.domain;

public enum TodoStatus {
    NOT_STARTED("시작전"),
    IN_PROGRESS("진행중"),
    DONE("완료");

    private final String displayName;

    TodoStatus(String displayName) {}
    public String getDisplayName() {}
}
```

### Todo.java

```java
package com.todolist.domain;

import java.time.LocalDateTime;

public class Todo {
    private final long id;
    private String content;
    private TodoStatus status;      // 기본값: NOT_STARTED
    private int priority;           // 0, 1, 2...
    private final LocalDateTime createdAt;

    public Todo(long id, String content, int priority) {}

    public void updateContent(String content) {}
    public void updateStatus(TodoStatus status) {}
    public void updatePriority(int priority) {}

    public long getId() {}
    public String getContent() {}
    public TodoStatus getStatus() {}
    public int getPriority() {}
    public String getFormattedCreatedAt() {}  // yyyy-MM-dd HH:mm
}
```

---

## 리포지토리 설계

### TodoRepository.java (인터페이스)

```java
package com.todolist.repository;

public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(long id);
    List<Todo> findAll();
    List<Todo> findByStatus(TodoStatus status);
    List<Todo> findByKeyword(String keyword);
    List<Todo> findByPriority(int priority);
    boolean deleteById(long id);
}
```

### InMemoryTodoRepository.java (구현체)

```java
package com.todolist.repository;

// ArrayList 기반 In-Memory 저장
// ID 시퀀스는 Repository가 직접 관리
public class InMemoryTodoRepository implements TodoRepository {
    private final List<Todo> storage = new ArrayList<>();
    private long idSequence = 1;

    // TodoRepository 인터페이스 전부 구현
}
```

---

## 서비스 설계

### TodoService.java

```java
package com.todolist.service;

// 생성자 주입으로 Repository 받음
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {}

    public Todo create(String content, int priority) {}  // 입력값 검증 포함
    public List<Todo> findAll() {}
    public Todo update(long id, String content, TodoStatus status, int priority) {}
    public void delete(long id) {}
    public List<Todo> searchByKeyword(String keyword) {}
    public List<Todo> filterByStatus(TodoStatus status) {}
    public List<Todo> filterByPriority(int priority) {}
}
```

---

## 예외 설계

### ErrorCode.java

```java
package com.todolist.common;

public enum ErrorCode {
    INVALID_SELECTION("잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요."),
    TODO_NOT_FOUND("해당 항목을 찾을 수 없습니다."),
    INVALID_INPUT("입력값이 올바르지 않습니다.");

    private final String message;

    ErrorCode(String message) {}
    public String getMessage() {}
}
```

### CustomException.java

```java
package com.todolist.common;

public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
```

---

## UI 설계

### 메뉴 구조

```
[ To-Do List Manager ]
1. 할 일 목록 보기
2. 새 할 일 추가
3. 할 일 수정
4. 할 일 삭제
5. 검색
6. 종료
선택 > _
```

### 할 일 목록 출력 형식

```
=============================================================================================
 ID |   상태   | 우선순위 |      생성 일시      |  할 일 내용
---------------------------------------------------------------------------------------------
 01 | [시작전] |    0    |  2024-05-22 09:00  | Java 인터페이스 설계 및 문서화
 02 | [진행중] |    1    |  2024-05-22 10:30  | Console UI 레이아웃 코드 구현
 03 | [ 완료 ] |    2    |  2024-05-21 14:00  | 요구사항 정의서 작성 완료
---------------------------------------------------------------------------------------------
[ 총 3개의 항목이 있습니다. ]
=============================================================================================
```

### 할 일 추가

```
[ 새 할 일 추가 ]
---------------------------------------------------------
> 할 일 내용 입력: Spring Boot 학습하기
> 우선순위 입력 (숫자 입력): 1
---------------------------------------------------------
[시스템] "Spring Boot 학습하기" 항목이 추가되었습니다!
(생성 시간: 2024-05-22 11:45)
---------------------------------------------------------
```

### 할 일 수정

```
[ 할 일 수정 ]
---------------------------------------------------------
> 수정할 항목의 ID를 입력하세요: 2

[현재 데이터]
- 내용: Console UI 레이아웃 코드 구현
- 상태: 진행 중
- 우선순위: 2

[변경 정보 입력]
1. 내용 수정: Console UI 및 예외 처리 구현
2. 상태 변경 (1:시작전, 2:진행중, 3:완료): 3
3. 우선순위 변경: 3

[시스템] ID 02번 항목의 정보가 성공적으로 업데이트되었습니다.
---------------------------------------------------------
```

### 할 일 삭제

```
[ 할 일 삭제 ]
---------------------------------------------------------
> 삭제할 항목의 ID를 입력하세요: 2

[시스템] ID 02번 항목의 정보가 성공적으로 삭제되었습니다.
---------------------------------------------------------
```

### 검색 및 필터링 메뉴

```
[ 검색 및 필터링 ]
---------------------------------------------------------
1. 키워드 검색 (할 일 내용)
2. 상태별 필터 (시작 전 / 진행 중 / 완료)
3. 우선순위별 필터 (숫자)
4. 뒤로 가기
---------------------------------------------------------
선택 > _
```

### 키워드 검색 결과

```
[ 키워드 검색 ]
> 검색할 단어를 입력하세요: 자바

[ '자바' 검색 결과 ]
=============================================================================================
 ID |   상태   | 우선순위 |      생성 일시      |  할 일 내용
---------------------------------------------------------------------------------------------
 01 | [시작전] |    1    |  2024-05-22 09:00  | Java 인터페이스 설계 및 문서화
 05 | [ 완료 ] |    2    |  2024-05-20 15:30  | 자바 기본 문법 복습
---------------------------------------------------------------------------------------------
[ 총 2개의 항목이 검색되었습니다. ]
=============================================================================================
```

### 상태별 필터링 결과

```
[ 상태별 필터링 ]
1: 시작 전, 2: 진행 중, 3: 완료
번호 선택 > 3

[ '완료' 상태 항목 목록 ]
=============================================================================================
 ID |   상태   | 우선순위 |      생성 일시      |  할 일 내용
---------------------------------------------------------------------------------------------
 03 | [ 완료 ] |    3    |  2024-05-21 14:00  | 요구사항 정의서 작성 완료
 05 | [ 완료 ] |    2    |  2024-05-20 15:30  | 자바 기본 문법 복습
---------------------------------------------------------------------------------------------
[ 총 2개의 항목이 검색되었습니다. ]
=============================================================================================
```

### 시스템 메시지 형식

```
[시스템] 성공적으로 추가되었습니다!
[시스템] 잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요.
현재 등록된 할 일이 없습니다.
```

### MenuInputView.java

```java
package com.todolist.view;

// 입력 + 유효성 검증 담당
// Scanner를 파라미터로 받음 (소유하지 않음)
public class MenuInputView {
    public String readSelection(Scanner scanner) {}  // 메뉴 선택 입력 + 검증
    public String readContent(Scanner scanner) {}    // 할 일 내용 입력
    public int readPriority(Scanner scanner) {}      // 우선순위 입력
    public long readId(Scanner scanner) {}           // ID 입력
    public String readKeyword(Scanner scanner) {}    // 검색 키워드 입력
}
```

### MenuViewOutputView.java

```java
package com.todolist.view;

// 출력만 담당
public class MenuViewOutputView {
    public void printMainMenu() {}                        // 메인 메뉴 출력
    public void printTodoList(List<Todo> todos) {}        // 목록 테이블 출력
    public void printNotImplementedMessage() {}           // 미구현 안내
    public void printErrorMessage(String message) {}      // 에러 메시지 출력
    public void printSuccessMessage(String message) {}    // 성공 메시지 출력
    public void printEmptyMessage() {}                    // 빈 목록 안내
}
```

---

## 의존성 흐름

```
TodoListApplication (진입점)
 ├── Scanner (직접 소유)
 ├── MenuViewOutputView (출력)
 └── MenuInputView (입력 + 검증)
      └── TodoService
           └── TodoRepository (InMemoryTodoRepository)
                └── Todo, TodoStatus (domain)
```

의존성 방향: view → service → repository → domain
공통 모듈: common (어느 레이어에서든 참조 가능)

---

## 구현 로드맵

```
Phase 1 - 도메인 모델
  └── TodoStatus → Todo

Phase 2 - 리포지토리
  └── TodoRepository (인터페이스) → InMemoryTodoRepository (구현체)

Phase 3 - 예외
  └── ErrorCode → CustomException

Phase 4 - 서비스
  └── TodoService + CustomException 연동

Phase 5 - UI
  └── MenuViewOutputView → MenuInputView → TodoListApplication

Phase 6 - 통합
  └── TodoListApplication에서 의존성 직접 연결 후 전체 흐름 검증

Phase 7 - 테스트 & 리팩터링
  └── JUnit 단위 테스트 작성
```

---

## 브랜치 전략

```
main     ← 안정된 코드만
feature  ← 기능 개발 (feature/todo-crud 등)
refactor ← 리팩터링
```

---

## 커밋 컨벤션

| 타입 | 설명 |
|------|------|
| feat | 새 기능 추가 |
| fix | 버그 수정 |
| refactor | 코드 구조 개선 |
| chore | 빌드 설정, 의존성 등 |
| test | 테스트 코드 |
| docs | 문서 수정 |