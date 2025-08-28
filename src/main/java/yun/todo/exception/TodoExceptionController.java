package yun.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionController {

    @ExceptionHandler(NoSuchTodoException.class)
    public ResponseEntity<ExceptionMessage> handleNoTodo(NoSuchTodoException exception) {
        ExceptionMessage message = new ExceptionMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
