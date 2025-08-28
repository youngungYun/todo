package yun.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import yun.todo.domain.Todo;

import java.time.LocalDateTime;

public record TodoUpdateRequest(
        @NotNull(message = "{todo.id.notnull}") Long id,
        @NotBlank(message = "{todo.description.notblank}") String description,
        @NotNull(message = "{todo.deadline.notnull}") LocalDateTime deadline
) {

    public Todo toEntity() {

        return Todo.builder().id(id).description(description).deadline(deadline).build();
    }
}

