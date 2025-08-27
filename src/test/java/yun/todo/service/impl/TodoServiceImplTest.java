package yun.todo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import yun.todo.domain.Todo;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoDeleteRequest;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateReqeust;
import yun.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;


class TodoServiceImplTest {

    private TodoRepository todoRepository;
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        todoRepository = mock(TodoRepository.class);
        todoService = new TodoServiceImpl(todoRepository);
    }

    @Test
    void readAll() {
        // given
        String description1 = "desctription1";
        String description2 = "desctription2";

        Todo todo1 = new Todo(1L, description1, LocalDateTime.now());
        Todo todo2 = new Todo(2L, description2, LocalDateTime.now());

        when(todoRepository.findAll()).thenReturn(List.of(todo1, todo2));

        // when
        List<TodoResponse> result = todoService.readAll();

        // then
        assertThat(result)
                .hasSize(2)
                .extracting(TodoResponse::description)
                .containsExactly(description1, description2);
    }


    @Test
    void create() {
        // given
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);
        Todo todo = request.toEntity();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoResponse result = todoService.create(request);

        // then
        assertThat(result)
                .extracting(TodoResponse::description, TodoResponse::deadline)
                .containsExactly(description, deadline);
    }

    @Test
    void update() {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoUpdateReqeust request = new TodoUpdateReqeust(id, description, deadline);
        Todo todo = request.toEntity();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoResponse result = todoService.update(request);

        // then
        assertThat(result)
                .extracting(TodoResponse::id, TodoResponse::description, TodoResponse::deadline)
                .containsExactly(id, description, deadline);
    }

    @Test
    void deleteSuccess() {
        // given
        Long id = 1L;

        TodoDeleteRequest request = new TodoDeleteRequest(id);

        // when
        todoService.delete(request);

        // then
        verify(todoRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteFailure() {
        // given
        Long id = 1L;

        TodoDeleteRequest request = new TodoDeleteRequest(id);

        doThrow(new EmptyResultDataAccessException(1))
                .when(todoRepository).deleteById(id);

        // when, then
        assertThatThrownBy(() -> todoService.delete(request))
                .isInstanceOf(EmptyResultDataAccessException.class);

        verify(todoRepository, times(1)).deleteById(id);
    }


    private LocalDateTime createDeadline() {
        return LocalDateTime.of(2025, 8, 27, 23, 58, 30);
    }
}