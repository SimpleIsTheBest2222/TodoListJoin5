package com.todolist.repository;

import com.todolist.domain.Todo;
import com.todolist.domain.TodoStatus;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(long id);
    List<Todo> findAll();
    List<Todo> findByStatus(TodoStatus status);
    List<Todo> findByKeyword(String keyword);
    List<Todo> findByPriority(int priority);
    // boolean인 이유 => ?
    boolean deleteById(long id);
}
