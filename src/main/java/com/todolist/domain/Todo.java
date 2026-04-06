package com.todolist.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Todo {
    private final long id;
    private String content;
    private TodoStatus status;
    private int priority;
    private final LocalDateTime createdAt;

    public Todo(long id, String content, int priority) {
        this.id = id;
        this.content = content;
        this.status = TodoStatus.NOT_STARTED; // 초기값
        this.priority = priority;
        this.createdAt = LocalDateTime.now(); // 초기값
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStatus(TodoStatus status) {
        this.status = status;
    }

    public void updatePriority(int priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}