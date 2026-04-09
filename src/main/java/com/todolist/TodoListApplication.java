package com.todolist;

import com.todolist.common.CustomException;
import com.todolist.domain.Todo;
import com.todolist.domain.TodoStatus;
import com.todolist.repository.InMemoryTodoRepository;
import com.todolist.service.TodoService;
import com.todolist.view.MenuInputView;
import com.todolist.view.MenuViewOutputView;

import java.util.List;
import java.util.Scanner;

public class TodoListApplication {

    public static void main(String[] args) {

        // 의존성 직접 조립
        Scanner scanner = new Scanner(System.in);
        MenuViewOutputView outputView = new MenuViewOutputView();
        MenuInputView inputView = new MenuInputView();
        TodoService todoService = new TodoService(new InMemoryTodoRepository());

        boolean running = true;

        while (running) {
            outputView.printMainMenu();
            try {
                String selection = inputView.readSelection(scanner);
                switch (selection) {
                    case "1" -> handleFindAll(todoService, outputView);
                    case "2" -> handleCreate(scanner, todoService, inputView, outputView);
                    case "3" -> handleUpdate(scanner, todoService, inputView, outputView);
                    case "4" -> handleDelete(scanner, todoService, inputView, outputView);
                    case "5" -> handleSearch(scanner, todoService, inputView, outputView);
                    case "6" -> running = false;
                }
            } catch (CustomException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }

        scanner.close();
        System.out.println("[시스템] 프로그램을 종료합니다.");
    }

    // 1. 전체 조회
    private static void handleFindAll(TodoService todoService,
                                      MenuViewOutputView outputView) {
        List<Todo> todos = todoService.findAll();
        if (todos.isEmpty()) {
            outputView.printEmptyMessage();
        } else {
            outputView.printTodoList(todos);
        }
    }

    // 2. 추가
    private static void handleCreate(Scanner scanner,
                                     TodoService todoService,
                                     MenuInputView inputView,
                                     MenuViewOutputView outputView) {
        System.out.println("\n[ 새 할 일 추가 ]");
        outputView.printLine();

        String content = inputView.readContent(scanner);
        int priority = inputView.readPriority(scanner);

        Todo created = todoService.create(content, priority);

        outputView.printLine();
        outputView.printSuccessMessage(
                "\"" + created.getContent() + "\" 항목이 추가되었습니다!\n" +
                        "(생성 시간: " + created.getFormattedCreatedAt() + ")"
        );
        outputView.printLine();
    }

    // 3. 수정
    private static void handleUpdate(Scanner scanner,
                                     TodoService todoService,
                                     MenuInputView inputView,
                                     MenuViewOutputView outputView) {
        System.out.println("\n[ 할 일 수정 ]");
        outputView.printLine();

        long id = inputView.readId(scanner);

        // 현재 데이터 출력 (findAll에서 찾거나 서비스에 findById 추가 권장)
        List<Todo> all = todoService.findAll();
        all.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .ifPresent(t -> {
                    System.out.println("\n[현재 데이터]");
                    System.out.println("- 내용: " + t.getContent());
                    System.out.println("- 상태: " + t.getStatus().getDisplayName());
                    System.out.println("- 우선순위: " + t.getPriority());
                });

        System.out.println("\n[변경 정보 입력]");
        String content = inputView.readContent(scanner);
        TodoStatus status = inputView.readStatus(scanner);
        int priority = inputView.readPriority(scanner);

        todoService.update(id, content, status, priority);

        outputView.printLine();
        outputView.printSuccessMessage("ID " + id + "번 항목의 정보가 성공적으로 업데이트되었습니다.");
        outputView.printLine();
    }

    // 4. 삭제
    private static void handleDelete(Scanner scanner,
                                     TodoService todoService,
                                     MenuInputView inputView,
                                     MenuViewOutputView outputView) {
        System.out.println("\n[ 할 일 삭제 ]");
        outputView.printLine();

        long id = inputView.readId(scanner);
        todoService.delete(id);

        outputView.printLine();
        outputView.printSuccessMessage("ID " + id + "번 항목이 성공적으로 삭제되었습니다.");
        outputView.printLine();
    }

    // 5. 검색 및 필터링
    private static void handleSearch(Scanner scanner,
                                     TodoService todoService,
                                     MenuInputView inputView,
                                     MenuViewOutputView outputView) {
        outputView.printSearchMenu();
        try {
            String selection = inputView.readSearchSelection(scanner);
            switch (selection) {
                case "1" -> {
                    // 키워드 검색
                    String keyword = inputView.readKeyword(scanner);
                    List<Todo> result = todoService.searchByKeyword(keyword);
                    if (result.isEmpty()) {
                        outputView.printEmptyMessage();
                    } else {
                        outputView.printTodoList(result);
                    }
                }
                case "2" -> {
                    // 상태 필터
                    System.out.println("1: 시작 전, 2: 진행 중, 3: 완료");
                    TodoStatus status = inputView.readStatus(scanner);
                    List<Todo> result = todoService.filterByStatus(status);
                    if (result.isEmpty()) {
                        outputView.printEmptyMessage();
                    } else {
                        outputView.printTodoList(result);
                    }
                }
                case "3" -> {
                    // 우선순위 필터
                    int priority = inputView.readPriority(scanner);
                    List<Todo> result = todoService.filterByPriority(priority);
                    if (result.isEmpty()) {
                        outputView.printEmptyMessage();
                    } else {
                        outputView.printTodoList(result);
                    }
                }
                case "4" -> { /* 뒤로 가기 */ }
            }
        } catch (CustomException e) {
            outputView.printErrorMessage(e.getMessage());
        }
    }
}