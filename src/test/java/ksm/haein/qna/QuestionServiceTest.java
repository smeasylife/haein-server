package ksm.haein.qna;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.qna.dto.QuestionSaveData;
import ksm.haein.qna.entity.Question;
import ksm.haein.qna.repository.QuestionRepository;
import ksm.haein.qna.service.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Q&A 서비스 테스트")
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    @DisplayName("질문 저장 - 성공")
    void testSaveQuestionSuccess() {
        // Given
        QuestionSaveData questionData = new QuestionSaveData("상품 문의", "배송 언제 되나요?");
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        questionService.save(questionData);

        // Then
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    @DisplayName("답변 저장 - 성공")
    void testSaveAnswerSuccess() {
        // Given
        Long questionId = 1L;
        String answer = "배송은 2~3일 소요됩니다.";

        Question mockQuestion = Question.builder()
                .id(questionId)
                .title("상품 문의")
                .content("배송 언제 되나요?")
                .build();

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(mockQuestion));

        // When
        questionService.saveAnswer(questionId, answer);

        // Then
        verify(questionRepository).findById(questionId);
        assertEquals(answer, mockQuestion.getAnswer());
    }

    @Test
    @DisplayName("답변 저장 - 실패 (존재하지 않는 질문)")
    void testSaveAnswerQuestionNotFound() {
        // Given
        Long questionId = 999L;
        String answer = "배송은 2~3일 소요됩니다.";

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> questionService.saveAnswer(questionId, answer));
        verify(questionRepository).findById(questionId);
    }
}
