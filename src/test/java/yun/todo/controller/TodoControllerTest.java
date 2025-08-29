package yun.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import yun.todo.dto.TodoCreateRequest;
import yun.todo.dto.TodoCreateResponse;
import yun.todo.dto.TodoResponse;
import yun.todo.dto.TodoUpdateRequest;
import yun.todo.exception.ErrorCode;
import yun.todo.exception.NoSuchTodoException;
import yun.todo.service.TodoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
class TodoControllerTest {

    String idMessage = "id가 없습니다.";
    String descriptionMessage = "설명을 입력하세요.";
    String deadlineMessage = "시간을 입력하세요.";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Test
    @DisplayName("GET /todo")
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
        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(id1))
                .andExpect(jsonPath("$[0].description").value(description1))
                .andExpect(jsonPath("$[0].deadline").value(deadline1.toString()))

                .andExpect(jsonPath("$[1].id").value(id2))
                .andExpect(jsonPath("$[1].description").value(description2))
                .andExpect(jsonPath("$[1].deadline").value(deadline2.toString()))

                .andDo(document("todo-get",
                        responseFields(
                                fieldWithPath("[].id").description("Todo의 ID"),
                                fieldWithPath("[].description").description("Todo의 내용"),
                                fieldWithPath("[].deadline").description("Todo의 데드라인")
                        )
                ));
    }

    @Test
    @DisplayName("POST /todo")
    void createTodo() throws Exception {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoCreateRequest request = new TodoCreateRequest(description, deadline);
        TodoCreateResponse response = new TodoCreateResponse(id);

        when(todoService.createTodo(any(TodoCreateRequest.class))).thenReturn(response);

        // when, then
        mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))

                .andDo(document("todo-post",
                        requestFields(
                                fieldWithPath("description").description("생성할 Todo의 설명"),
                                fieldWithPath("deadline").description("생성할 Todo의 데드라인")),
                        responseFields(
                                fieldWithPath("id").description("생성된 Todo의 ID")
                        )
                ));
    }

    @Test
    @DisplayName("POST /todo 유효성 검증 실패")
    void createTodo_ValidFailure() throws Exception {
        // given
        Long id = 1L;
        // description이 " "
        String description = " ";
        // deadline이 null
        LocalDateTime deadline = null;


        TodoCreateRequest request = new TodoCreateRequest(description, deadline);
        TodoCreateResponse response = new TodoCreateResponse(id);

        when(todoService.createTodo(any(TodoCreateRequest.class))).thenReturn(response);

        // when, then
        mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.description").value(descriptionMessage))
                .andExpect(jsonPath("$.deadline").value(deadlineMessage))

                .andDo(document("todo-post-valid_failure",
                        requestFields(
                                fieldWithPath("description").description("생성할 Todo의 설명"),
                                fieldWithPath("deadline").description("생성할 Todo의 데드라인")),
                        responseFields(
                                fieldWithPath("description").description("description의 유효성 메시지"),
                                fieldWithPath("deadline").description("deadline의 유효성 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("PUT /todo")
    void updateTodo() throws Exception {
        // given
        Long id = 1L;
        String description = "description";
        LocalDateTime deadline = createDeadline();

        TodoUpdateRequest request = new TodoUpdateRequest(id, description, deadline);

        // when, then
        mockMvc.perform(put("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())

                .andDo(document("todo-put",
                        requestFields(
                                fieldWithPath("id").description("수정할 Todo의 ID"),
                                fieldWithPath("description").description("수정할 Todo의 설명"),
                                fieldWithPath("deadline").description("수정할 Todo의 데드라인"))
                ));
    }

    @Test
    @DisplayName("PUT /todo 유효성 검증 실패")
    void updateTodo_ValidFailure() throws Exception {

        // given
        // id가 null
        Long id = null;
        // description이 " "
        String description = " ";
        // deadline이 null
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
                .andExpect(jsonPath("$.deadline").value(deadlineMessage))

                .andDo(document("todo-put-valid_failure",
                    requestFields(
                            fieldWithPath("id").description("수정할 Todo의 ID"),
                            fieldWithPath("description").description("수정할 Todo의 설명"),
                            fieldWithPath("deadline").description("수정할 Todo의 데드라인")),
                    responseFields(
                            fieldWithPath("id").description("id의 유효성 메시지"),
                            fieldWithPath("description").description("description의 유효성 메시지"),
                            fieldWithPath("deadline").description("deadline의 유효성 메시지")
                    )
        ));
    }

    @Test
    @DisplayName("DELETE /todo/{id}")
    void deleteTodo_Success() throws Exception {

        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(delete("/todo/{id}", id))
                .andExpect(status().isNoContent())

                .andDo(document("todo-delete",
                        pathParameters(
                            parameterWithName("id").description("삭제할 Todo의 ID")
                        )
                ));

    }

    @Test
    @DisplayName("DELETE /todo/{id} 존재하지 않는 id라 예외 발생")
    void deleteTodo_Failure() throws Exception {

        // given
        Long id = 1L;

        doThrow(new NoSuchTodoException())
                .when(todoService).deleteTodo(id);

        // when, then
        mockMvc.perform(delete("/todo/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(ErrorCode.NO_SUCH_TODO.getDescription()))

                .andDo(document("todo-delete-failure",
                        pathParameters(
                                parameterWithName("id").description("삭제할 Todo의 Id")
                        ),
                        responseFields(
                                fieldWithPath("message").description("예외 메시지")
                        )
                ));
    }

    private LocalDateTime createDeadline() {
        return LocalDateTime.of(2025, 12, 25, 6, 30, 10);
    }
}