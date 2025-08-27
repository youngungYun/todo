package yun.todo.service;

import yun.todo.dto.TodoRequest;

import java.util.List;

public interface TodoService {

    List<TodoResponse> readAllTodos();

    TodoResponse createTodo(TodoRequest request);

    TodoResponse updateTodo(TodoRequest request);

    void deleteTodo(TodoRequest request);
}
