package ksm.haein.qna.dto;

import ksm.haein.qna.entity.Question;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDto {
    private String question;
    private String answer;
    private LocalDateTime createdAt;

    public QuestionDto(Question question) {
        this.question = question.getQuestion();
        this.answer = question.getAnswer();
        this.createdAt = question.getCreatedAt();
    }
}
