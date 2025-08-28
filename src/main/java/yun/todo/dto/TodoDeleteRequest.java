package yun.todo.dto;

import jakarta.validation.constraints.NotNull;

public record TodoDeleteRequest(
        @NotNull Long id
) { }
