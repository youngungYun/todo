package yun.todo.service;

import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.NoSuchTodoException;

import java.util.List;

public interface TodoService {

    List<TodoResponse> getTodos();

    TodoResponse createTodo(TodoCreateRequest request);

    TodoResponse updateTodo(TodoUpdateRequest request);

    void deleteTodo(Long id) throws NoSuchTodoException;
}
