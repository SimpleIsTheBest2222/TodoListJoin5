package com.todolist.domain;

public class TodoCreateRequest {
    private final String content;
    private final int priority;

    public TodoCreateRequest(String content, int priority) {
        this.content = content;
        this.priority = priority;
    }

    public String getContent() { return content; }
    public int getPriority() { return priority; }
}
