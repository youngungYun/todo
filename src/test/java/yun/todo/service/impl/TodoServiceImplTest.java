package yun.todo.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yun.todo.domain.Todo;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoCreateResponse;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.NoSuchTodoException;
import yun.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    @DisplayName("모든 Todo 조회")
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
    @DisplayName("Todo 생성")
    void createTodo() {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createTodoDeadline();

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);
        Todo todo = request.toEntity();
        todo.setId(id);

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        TodoCreateResponse result = todoService.createTodo(request);

        // then
        assertThat(result)
                .extracting(TodoCreateResponse::id)
                .isEqualTo(id);
    }

    @Test
    @DisplayName("Todo 수정")
    void updateTodo() {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createTodoDeadline();

        TodoUpdateRequest request = new TodoUpdateRequest(id, description, deadline);
        Todo todo = request.toEntity();

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // when
        todoService.updateTodo(request);

        // then
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Todo 삭제")
    void deleteTodo_Success() {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createTodoDeadline();

        Todo todo = Todo.builder().id(id).description(description).deadline(deadline).build();

        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(todo));

        // when
        todoService.deleteTodo(id);

        // then
        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).delete(todo);

    }

    @Test
    @DisplayName("삭제할 id를 가진 Todo 없어 예외발생")
    void deleteTodo_Failure() {
        // given
        Long id = 1L;

        when(todoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> todoService.deleteTodo(id))
                .isInstanceOf(NoSuchTodoException.class);

        verify(todoRepository, times(1)).findById(id);
    }


    private LocalDateTime createTodoDeadline() {
        return LocalDateTime.of(2025, 8, 27, 23, 58, 30);
    }
}