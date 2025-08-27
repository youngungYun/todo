package yun.todo.dto;

import yun.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        String description,
        LocalDateTime deadline
) {

    public TodoResponse(Todo todo) {
        this(
                todo.getId(),
                todo.getDescription(),
                todo.getDeadline()
        );
    }
}
