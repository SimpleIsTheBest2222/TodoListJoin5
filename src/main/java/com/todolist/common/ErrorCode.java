package com.todolist.common;

public enum ErrorCode {

    // 메뉴 선택 범위 초과 시
    INVALID_SELECTION("잘못된 입력입니다. 1~6 사이의 숫자를 입력해주세요."),

    // ID 조회 실패 시
    TODO_NOT_FOUND("해당 항목을 찾을 수 없습니다."),

    // 빈 문자열, 숫자 형식 오류 등 입력값 문제
    INVALID_INPUT("입력값이 올바르지 않습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}