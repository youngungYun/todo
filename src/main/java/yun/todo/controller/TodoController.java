package yun.todo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yun.todo.dto.TodoCreateRequest;
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
    public ResponseEntity<TodoResponse> createTodo(@RequestBody @Valid TodoCreateRequest request) {
        var createdTodo = todoService.createTodo(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdTodo);
    }

    @PutMapping(produces = {"application/json"})
    public ResponseEntity<TodoResponse> updateTodo(@RequestBody @Valid TodoUpdateRequest request) {
        var updatedTodo = todoService.updateTodo(request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(updatedTodo);
    }

    @DeleteMapping(path = "/{id}", produces = {"application/json"})
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
