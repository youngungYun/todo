package yun.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Todo API", description = "Todo API입니다.")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(produces = {"application/json"})
    @Tag(name = "Todo API")
    @Operation(summary = "모든 Todo 불러옴")
    @ApiResponses(value =
    @ApiResponse(responseCode = "200", description = "모든 Todo 가져옴"))
    public ResponseEntity<List<TodoResponse>> getTodos() {
        var todoList = todoService.getTodos();

        return ResponseEntity.ok()
                .body(todoList);
    }

    @PostMapping(produces = {"application/json"})
    @Operation(summary = "Todo 생성")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Todo 생성 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패", content = @Content())})
    public ResponseEntity<TodoCreateResponse> createTodo(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "생성할 Todo의 내용과 날짜")
                                                             @RequestBody @Valid TodoCreateRequest request) {
        TodoCreateResponse response = todoService.createTodo(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(produces = {"application/json"})
    @Operation(summary = "Todo 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Todo 수정 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 실패")})
    public ResponseEntity<Void> updateTodo(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "수정할 Todo의 id와 수정 내용 및 수정 날짜")
                                               @RequestBody @Valid TodoUpdateRequest request) {
        todoService.updateTodo(request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @DeleteMapping(path = "/{id}", produces = {"application/json"})
    @Operation(summary = "Todo 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Todo 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "존재하지 않는 Todo")})
    public ResponseEntity<Void> deleteTodo(@Parameter(description = "삭제할 Todo의 id")
                                               @PathVariable Long id) {
        todoService.deleteTodo(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
