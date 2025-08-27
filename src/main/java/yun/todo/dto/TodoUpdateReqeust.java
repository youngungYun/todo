package yun.todo.dto;

import yun.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoUpdateReqeust(
        Long id,
        String description,
        LocalDateTime deadline
) {

    public Todo toEntity() {
        Todo todo = Todo.builder().id(id).description(description).deadline(deadline).build();

        return todo;
    }
}

