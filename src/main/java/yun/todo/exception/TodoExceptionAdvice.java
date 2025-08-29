package yun.todo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class TodoExceptionAdvice {

    @ExceptionHandler(NoSuchTodoException.class)
    public ResponseEntity<Map<String, String>> handleNoTodo(NoSuchTodoException exception) {
        var errors = exceptionToMap(exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleNotValid(MethodArgumentNotValidException exception) {
        var errors = exceptionToMap(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

    }

    private Map<String, String> exceptionToMap(RuntimeException exception) {
        return Map.of("message", exception.getMessage());
    }

    // validation 예외 처리
    private Map<String, Object> exceptionToMap(MethodArgumentNotValidException exception) {
        Map<String, Object> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        log.warn("[Validation Failed] Errors: {}", errors.toString());

        return errors;
    }
}
