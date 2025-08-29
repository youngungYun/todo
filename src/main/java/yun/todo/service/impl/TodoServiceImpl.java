package yun.todo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import yun.todo.domain.Todo;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoCreateResponse;
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
    public TodoCreateResponse createTodo(TodoCreateRequest request) {
        Todo savedTodo = repository.save(request.toEntity());

        return new TodoCreateResponse(savedTodo.getId());
    }

    public void updateTodo(TodoUpdateRequest request) {
        repository.save(request.toEntity());
    }

    @Override
    public void deleteTodo(@NonNull Long id) throws NoSuchTodoException {
        Todo todo = repository.findById(id)
                .orElseThrow(NoSuchTodoException::new);

        repository.delete(todo);
    }
}
