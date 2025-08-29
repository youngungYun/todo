package yun.todo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoCreateResponse;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<TodoResponse>> getTodos() {
        var todoList = todoService.getTodos();

        return ResponseEntity.ok()
                .body(todoList);
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<TodoCreateResponse> createTodo(@RequestBody @Valid TodoCreateRequest request) {
        TodoCreateResponse response = todoService.createTodo(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(produces = {"application/json"})
    public ResponseEntity<Void> updateTodo(@RequestBody @Valid TodoUpdateRequest request) {
        todoService.updateTodo(request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @DeleteMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
