package com.todolist.view;

import com.todolist.common.CustomException;
import com.todolist.common.ErrorCode;
import com.todolist.domain.TodoStatus;

import java.util.Scanner;

public class MenuInputView {

    // 메인 메뉴 선택 (1~6)
    public String readSelection(Scanner scanner) {
        String input = scanner.nextLine().trim();
        if (!input.matches("[1-6]")) {
            throw new CustomException(ErrorCode.INVALID_SELECTION);
        }
        return input;
    }

    // 검색 서브 메뉴 선택 (1~4)
    public String readSearchSelection(Scanner scanner) {
        String input = scanner.nextLine().trim();
        if (!input.matches("[1-4]")) {
            throw new CustomException(ErrorCode.INVALID_SELECTION);
        }
        return input;
    }

    // 할 일 내용
    public String readContent(Scanner scanner) {
        System.out.print("> 할 일 내용 입력: ");
        String input = scanner.nextLine().trim();
        if (input.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        return input;
    }

    // 우선순위
    public int readPriority(Scanner scanner) {
        System.out.print("> 우선순위 입력 (숫자 입력): ");
        String input = scanner.nextLine().trim();
        try {
            int priority = Integer.parseInt(input);
            if (priority < 0) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }
            return priority;
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

    // ID
    public long readId(Scanner scanner) {
        System.out.print("> 항목의 ID를 입력하세요: ");
        String input = scanner.nextLine().trim();
        try {
            long id = Long.parseLong(input);
            if (id <= 0) {
                throw new CustomException(ErrorCode.INVALID_INPUT);
            }
            return id;
        } catch (NumberFormatException e) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

    // 키워드
    public String readKeyword(Scanner scanner) {
        System.out.print("> 검색할 단어를 입력하세요: ");
        String input = scanner.nextLine().trim();
        if (input.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        return input;
    }

    // 상태 선택 (1:시작전, 2:진행중, 3:완료)
    public TodoStatus readStatus(Scanner scanner) {
        System.out.print("> 상태 변경 (1:시작전, 2:진행중, 3:완료): ");
        String input = scanner.nextLine().trim();
        return switch (input) {
            case "1" -> TodoStatus.NOT_STARTED;
            case "2" -> TodoStatus.IN_PROGRESS;
            case "3" -> TodoStatus.DONE;
            default -> throw new CustomException(ErrorCode.INVALID_SELECTION);
        };
    }
}