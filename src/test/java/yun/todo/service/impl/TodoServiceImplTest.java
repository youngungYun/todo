package yun.todo.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import yun.todo.domain.Todo;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.NoSuchTodoException;
import yun.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void getTodos() {
        // given
        String description1 = "desctription1";
        String description2 = "desctription2";

        Todo todo1 = new Todo(1L, description1, LocalDateTime.now());
        Todo todo2 = new Todo(2L, description2, LocalDateTime.now());

        when(todoRepository.findAll()).thenReturn(List.of(todo1, todo2));

        // when
        List<TodoResponse> result = todoService.getTodos();

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(TodoResponse::description)
                .containsExactly(description1, description2);
    }


    @Test
    void createTodo() {
        // given
        String description = "description";
        LocalDateTime deadline = createTodoDeadline();

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);
        Todo todo = request.toEntity();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoResponse result = todoService.createTodo(request);

        // then
        assertThat(result)
                .extracting(TodoResponse::description, TodoResponse::deadline)
                .containsExactly(description, deadline);
    }

    @Test
    void updateTodo() {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createTodoDeadline();

        TodoUpdateRequest request = new TodoUpdateRequest(id, description, deadline);
        Todo todo = request.toEntity();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoResponse result = todoService.updateTodo(request);

        // then
        assertThat(result)
                .extracting(TodoResponse::id, TodoResponse::description, TodoResponse::deadline)
                .containsExactly(id, description, deadline);
    }

    @Test
    void deleteTodo_Success() {
        // given
        Long id = 1L;

        // when
        todoService.deleteTodo(id);

        // then
        verify(todoRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteTodo_Failure() {
        // given
        Long id = 1L;

        doThrow(new EmptyResultDataAccessException(1))
                .when(todoRepository).deleteById(id);

        // when, then
        assertThatThrownBy(() -> todoService.deleteTodo(id))
                .isInstanceOf(NoSuchTodoException.class);

        verify(todoRepository, times(1)).deleteById(id);
    }


    private LocalDateTime createTodoDeadline() {
        return LocalDateTime.of(2025, 8, 27, 23, 58, 30);
    }
}