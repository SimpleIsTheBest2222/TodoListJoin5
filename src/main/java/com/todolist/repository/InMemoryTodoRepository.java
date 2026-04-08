package com.todolist.repository;

import com.todolist.domain.Todo;
import com.todolist.domain.TodoStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryTodoRepository implements TodoRepository {
    private final List<Todo> storage = new ArrayList<>(); // not static -> 독립된 환경
    private static long idSequence = 1;

    @Override
    public Todo save(String content, int priority) {
        Todo todo = new Todo(idSequence++, content, priority);
        storage.add(todo);
        return todo;
    }

    @Override
    public Optional<Todo> findById(long id) {
        return storage.stream()
                .filter(t -> t.getId() == id)
                .findFirst(); // 필터링한 결과값의 첫 번째 값인 이유 => 유일성
    }

    @Override
    public List<Todo> findAll() {
        return Collections.unmodifiableList(storage); // READ-ONLY처리
    }

    @Override
    public List<Todo> findByStatus(TodoStatus status) {
        return storage.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList()); //필터링 결과를 리스트로 변환
    }

    @Override
    public List<Todo> findByKeyword(String keyword) {
        return storage.stream()
                .filter(t -> t.getContent().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Todo> findByPriority(int priority) {
        return storage.stream()
                .filter(t -> t.getPriority() == priority)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(long id) {
        return storage.removeIf(t -> t.getId() == id);
    }
}