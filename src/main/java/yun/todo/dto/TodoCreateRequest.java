package yun.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import yun.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoCreateRequest(
        @NotBlank(message = "{todo.description.notblank}") String description,
        @NotNull(message = "{todo.deadline.notnull}") LocalDateTime deadline){

    public Todo toEntity() {
        Todo todo = Todo.builder().description(description).deadline(deadline).build();

        return todo;
    }
}
