package com.todolist.service;

import com.todolist.common.CustomException;
import com.todolist.common.ErrorCode;
import com.todolist.domain.Todo;
import com.todolist.domain.TodoCreateRequest;
import com.todolist.domain.TodoStatus;
import com.todolist.repository.TodoRepository;

import java.util.List;

public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 생성 - 내용 빈값 검증
    public Todo create(String content, int priority) {
        if (content == null || content.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (priority < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        Todo todo = new Todo(0, content, priority); // ID는 Repository가 부여
        return todoRepository.save(new TodoCreateRequest(content, priority));
    }

    // 전체 조회
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    // 수정 - ID 없으면 예외
    public Todo update(long id, String content, TodoStatus status, int priority) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        if (content == null || content.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (priority < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        todo.updateContent(content);
        todo.updateStatus(status);
        todo.updatePriority(priority);
        return todo;
    }

    // 삭제 - ID 없으면 예외
    public void delete(long id) {
        boolean deleted = todoRepository.deleteById(id);
        if (!deleted) {
            throw new CustomException(ErrorCode.TODO_NOT_FOUND);
        }
    }

    // 키워드 검색
    public List<Todo> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        return todoRepository.findByKeyword(keyword);
    }

    // 상태 필터
    public List<Todo> filterByStatus(TodoStatus status) {
        return todoRepository.findByStatus(status);
    }

    // 우선순위 필터
    public List<Todo> filterByPriority(int priority) {
        if (priority < 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        return todoRepository.findByPriority(priority);
    }
}