package yun.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import yun.todo.controller.TodoController;
import yun.todo.domain.Todo;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.repository.TodoRepository;
import yun.todo.service.TodoService;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class TodoIntegrationTest {

    String idMessage = "id가 없습니다.";
    String descriptionMessage = "설명을 입력하세요.";
    String deadlineMessage = "시간을 입력하세요.";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoController controller;

    @Autowired
    private TodoService service;

    @Autowired
    private TodoRepository repository;

    @Test
    @DisplayName("저장된 모든 Todo 가져옴")
    void getTodos() throws Exception {
        // given
        String description1 = "description1";
        LocalDateTime deadline1 = createDeadline();

        String description2 = "description2";
        LocalDateTime deadline2 = createDeadline();

        Long id1 = saveTodo(description1, deadline1);
        Long id2 = saveTodo(description2, deadline2);

        // when, then
        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].description").value(description1))
                .andExpect(jsonPath("$[0].deadline").value(deadline1.toString()))

                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].description").value(description2))
                .andExpect(jsonPath("$[1].deadline").value(deadline2.toString()));
    }

    @Test
    @DisplayName("Todo 생성 성공")
    void createTodo_Success() throws Exception {
        // given
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);

        // when, then
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @DisplayName("Todo 생성 요청 유효성 검증 실패")
    void createTodo_ValidFailure() throws Exception {
        // given
        String description = " ";
        LocalDateTime deadline = null;

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);

        // when, then
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.description").value(descriptionMessage))
                .andExpect(jsonPath("$.deadline").value(deadlineMessage));
    }

    @Test
    @DisplayName("Todo 수정 성공")
    void updateTodo_Success() throws Exception {
        // given
        Long id = saveTodo("description", createDeadline());
        String description = "updated description";
        LocalDateTime deadline = createDeadline();

        TodoUpdateRequest request = new TodoUpdateRequest(id, description, deadline);

        // when, then
        mockMvc.perform(put("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Todo 수정 요청 유효성 검증 실패")
    void updateTodo_ValidFailure() throws Exception {
        // given
        saveTodo("description", createDeadline());
        Long id = null;
        String description = " ";
        LocalDateTime deadline = null;

        TodoUpdateRequest request = new TodoUpdateRequest(id, description, deadline);

        // when, then
        mockMvc.perform(put("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(idMessage))
                .andExpect(jsonPath("$.description").value(descriptionMessage))
                .andExpect(jsonPath("$.deadline").value(deadlineMessage));
    }

    @Test
    @DisplayName("Todo 삭제 성공")
    void deleteTodo_Success() throws Exception {
        // given
        String description = "description";
        LocalDateTime deadline = createDeadline();
        Long id = saveTodo(description, deadline);

        // when, then
        mockMvc.perform(delete("/todo/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 id로 인해 삭제 실패")
    void deleteTodo_Failure() throws Exception {
        // given
        Long id = 9999L;

        // when, then
        mockMvc.perform(delete("/todo/{id}", id))
                .andExpect(status().isNotFound());

    }

    private Long saveTodo(String description, LocalDateTime deadline) {
        Todo savedTodo = repository.save(Todo.builder().description(description).deadline(deadline).build());

        return savedTodo.getId();
    }

    private LocalDateTime createDeadline() {
        return LocalDateTime.of(2025, 12, 25, 20, 30, 10);
    }
}
