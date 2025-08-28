package yun.todo.exception;

import lombok.Getter;

@Getter
public class NoSuchTodoException extends RuntimeException {

    public NoSuchTodoException() {
        super(ErrorCode.NO_SUCH_TODO.getDescription());
    }
}
