package yun.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.NoSuchTodoException;
import yun.todo.service.TodoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Test
    void getTodos() throws Exception {
        // given
        Long id1 = 1L;
        String description1 = "description1";
        LocalDateTime deadline1 = createDeadline();

        Long id2 = 2L;
        String description2 = "description2";
        LocalDateTime deadline2 = createDeadline();

        TodoResponse response1 = new TodoResponse(id1, description1, deadline1);
        TodoResponse response2 = new TodoResponse(id2, description2, deadline2);
        List<TodoResponse> responseList = List.of(response1, response2);

        when(todoService.getTodos()).thenReturn(responseList);

        // when, then
        mockMvc.perform(get("/todo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].description").value(description1))
                .andExpect(jsonPath("$[0].deadline").value(deadline1.toString()))

                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].description").value(description2))
                .andExpect(jsonPath("$[1].deadline").value(deadline2.toString()));
    }

    @Test
    void createTodo() throws Exception {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);
        TodoResponse response = new TodoResponse(id, description, deadline);

        when(todoService.createTodo(any(TodoCreateRequest.class))).thenReturn(response);

        // when, then
        mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.deadline").value(deadline.toString()));
    }

    @Test
    void updateTodo() throws Exception {

        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoUpdateRequest request = new TodoUpdateRequest(id, description, deadline);
        TodoResponse response = new TodoResponse(id, description, deadline);

        when(todoService.updateTodo(any(TodoUpdateRequest.class))).thenReturn(response);

        // when, then
        mockMvc.perform(put("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.deadline").value(deadline.toString()));
    }

    @Test
    void deleteTodo_Success() throws Exception {

        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(delete("/todo/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTodo_Failure() throws Exception {

        // given
        Long id = 1L;

        doThrow(new NoSuchTodoException())
                .when(todoService).deleteTodo(id);

        // when, then
        mockMvc.perform(delete("/todo/{id}", id))
                .andExpect(status().isNotFound());
    }

    private LocalDateTime createDeadline() {
        return LocalDateTime.of(2025, 12, 25, 6, 30, 10);
    }
}