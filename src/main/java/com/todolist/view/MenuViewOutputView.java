package com.todolist.view;

import com.todolist.domain.Todo;

import java.util.List;

public class MenuViewOutputView {

    private static final String SEPARATOR =
            "=============================================================================================";
    private static final String DIVIDER =
            "---------------------------------------------------------------------------------------------";
    private static final String LINE =
            "---------------------------------------------------------";

    // 메인 메뉴
    public void printMainMenu() {
        System.out.println("\n[ To-Do List Manager ]");
        System.out.println("1. 할 일 목록 보기");
        System.out.println("2. 새 할 일 추가");
        System.out.println("3. 할 일 수정");
        System.out.println("4. 할 일 삭제");
        System.out.println("5. 검색");
        System.out.println("6. 종료");
        System.out.print("선택 > ");
    }

    // 검색/필터 서브 메뉴
    public void printSearchMenu() {
        System.out.println("\n[ 검색 및 필터링 ]");
        System.out.println(LINE);
        System.out.println("1. 키워드 검색 (할 일 내용)");
        System.out.println("2. 상태별 필터 (시작 전 / 진행 중 / 완료)");
        System.out.println("3. 우선순위별 필터 (숫자)");
        System.out.println("4. 뒤로 가기");
        System.out.println(LINE);
        System.out.print("선택 > ");
    }

    // 목록 테이블 출력
    public void printTodoList(List<Todo> todos) {
        System.out.println(SEPARATOR);
        System.out.printf(" %-4s| %-8s | %-8s | %-19s | %s%n",
                "ID", "상태", "우선순위", "생성 일시", "할 일 내용");
        System.out.println(DIVIDER);
        for (Todo todo : todos) {
            System.out.printf(" %-4d| [%-6s] | %-8d | %-19s | %s%n",
                    todo.getId(),
                    todo.getStatus().getDisplayName(),
                    todo.getPriority(),
                    todo.getFormattedCreatedAt(),
                    todo.getContent());
        }
        System.out.println(DIVIDER);
        System.out.printf("[ 총 %d개의 항목이 있습니다. ]%n", todos.size());
        System.out.println(SEPARATOR);
    }

    public void printSuccessMessage(String message) {
        System.out.println("[시스템] " + message);
    }

    public void printErrorMessage(String message) {
        System.out.println("[시스템] " + message);
    }

    public void printEmptyMessage() {
        System.out.println("현재 등록된 할 일이 없습니다.");
    }

    public void printNotImplementedMessage() {
        System.out.println("[시스템] 아직 구현되지 않은 기능입니다.");
    }

    public void printLine() {
        System.out.println(LINE);
    }
}