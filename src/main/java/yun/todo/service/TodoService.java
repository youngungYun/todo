package yun.todo.service;

import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoCreateResponse;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.NoSuchTodoException;

import java.util.List;

public interface TodoService {

    List<TodoResponse> getTodos();

    TodoCreateResponse createTodo(TodoCreateRequest request);

    void updateTodo(TodoUpdateRequest request);

    void deleteTodo(Long id) throws NoSuchTodoException;
}
