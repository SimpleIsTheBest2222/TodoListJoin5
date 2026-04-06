package com.todolist.domain;

public enum TodoStatus {
    NOT_STARTED("시작전"),
    IN_PROGRESS("진행중"),
    DONE("완료");

    private final String displayName;

    TodoStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}