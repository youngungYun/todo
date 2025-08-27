package yun.todo.dto;

import yun.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoRequest(
        String description,
        LocalDateTime deadline){

    Todo toEntity() {
        Todo todo = Todo.builder().description(description).deadline(deadline).build();

        return todo;
    }
}
