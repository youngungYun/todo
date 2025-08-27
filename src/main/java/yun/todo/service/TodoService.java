package yun.todo.service;

import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoDeleteRequest;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateReqeust;

import java.util.List;

public interface TodoService {

    List<TodoResponse> readAll();

    TodoResponse create(TodoCreateRequest request);

    TodoResponse update(TodoUpdateReqeust request);

    void delete(TodoDeleteRequest request);
}
