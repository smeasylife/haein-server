package ksm.haein.qna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksm.haein.qna.dto.QuestionSaveData;
import ksm.haein.qna.service.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
@DisplayName("Q&A 컨트롤러 테스트")
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionService questionService;

    @Test
    @DisplayName("질문 작성 - 성공")
    void testSaveQuestionSuccess() throws Exception {
        // Given
        QuestionSaveData questionData = new QuestionSaveData("상품 문의", "배송 언제 되나요?");
        doNothing().when(questionService).save(any(QuestionSaveData.class));

        // When & Then
        mockMvc.perform(post("/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(questionService).save(any(QuestionSaveData.class));
    }

    @Test
    @DisplayName("질문 작성 - 실패 (제목 누락)")
    void testSaveQuestionMissingTitle() throws Exception {
        // Given
        QuestionSaveData questionData = new QuestionSaveData("", "배송 언제 되나요?");

        // When & Then
        mockMvc.perform(post("/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("질문 작성 - 실패 (내용 누락)")
    void testSaveQuestionMissingContent() throws Exception {
        // Given
        QuestionSaveData questionData = new QuestionSaveData("상품 문의", "");

        // When & Then
        mockMvc.perform(post("/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("답변 작성 - 성공")
    void testSaveAnswerSuccess() throws Exception {
        // Given
        Long questionId = 1L;
        String answer = "배송은 2~3일 소요됩니다.";
        doNothing().when(questionService).saveAnswer(eq(questionId), eq(answer));

        // When & Then
        mockMvc.perform(post("/answer/{questionId}", questionId)
                        .param("answer", answer)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(questionService).saveAnswer(eq(questionId), eq(answer));
    }

    @Test
    @DisplayName("답변 작성 - 실패 (질문 ID 누락)")
    void testSaveAnswerMissingQuestionId() throws Exception {
        // Given
        String answer = "배송은 2~3일 소요됩니다.";

        // When & Then
        mockMvc.perform(post("/answer/{questionId}", "invalid")
                        .param("answer", answer)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("답변 작성 - 실패 (답변 내용 누락)")
    void testSaveAnswerMissingAnswer() throws Exception {
        // Given
        Long questionId = 1L;

        // When & Then
        mockMvc.perform(post("/answer/{questionId}", questionId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("답변 작성 - 실패 (존재하지 않는 질문)")
    void testSaveAnswerQuestionNotFound() throws Exception {
        // Given
        Long questionId = 999L;
        String answer = "배송은 2~3일 소요됩니다.";
        doThrow(new RuntimeException("Question not found"))
                .when(questionService).saveAnswer(eq(questionId), eq(answer));

        // When & Then
        mockMvc.perform(post("/answer/{questionId}", questionId)
                        .param("answer", answer)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
