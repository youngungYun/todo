package yun.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import yun.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoUpdateRequest(
        @NotNull Long id,
        @NotBlank String description,
        @NotNull LocalDateTime deadline
) {

    public Todo toEntity() {

        return Todo.builder().id(id).description(description).deadline(deadline).build();
    }
}

