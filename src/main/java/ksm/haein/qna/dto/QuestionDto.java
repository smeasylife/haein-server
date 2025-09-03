package ksm.haein.qna.dto;

import ksm.haein.qna.entity.Question;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionDto {
    private String content;
    private String answer;
    private LocalDateTime createdAt;

    public QuestionDto(Question content) {
        this.content = content.getContent();
        this.answer = content.getAnswer();
        this.createdAt = content.getCreatedAt();
    }
}
