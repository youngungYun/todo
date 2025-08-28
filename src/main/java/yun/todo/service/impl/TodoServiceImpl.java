package yun.todo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import yun.todo.domain.Todo;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.NoSuchTodoException;
import yun.todo.repository.TodoRepository;
import yun.todo.service.TodoService;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    @Autowired
    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TodoResponse> getTodos() {

        return repository.findAll().stream().map(todo -> {
            return new TodoResponse(todo.getId(), todo.getDescription(), todo.getDeadline());
        }
        ).toList();
    }

    @Override
    public TodoResponse createTodo(TodoCreateRequest request) {
        Todo savedTodo = repository.save(request.toEntity());

        return new TodoResponse(savedTodo.getId(), savedTodo.getDescription(), savedTodo.getDeadline());
    }

    public TodoResponse updateTodo(TodoUpdateRequest request) {
        Todo UpdatedTodo = repository.save(request.toEntity());

        return new TodoResponse(UpdatedTodo.getId(), UpdatedTodo.getDescription(), UpdatedTodo.getDeadline());

    }

    @Override
    public void deleteTodo(Long id) throws NoSuchTodoException {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new NoSuchTodoException();
        }
    }
}
