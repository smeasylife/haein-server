package ksm.haein.qna.service;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.qna.dto.QuestionSaveData;
import ksm.haein.qna.entity.Question;
import ksm.haein.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public void save(QuestionSaveData questionSaveData) {
        Question question = Question.builder()
                .title(questionSaveData.title())
                .content(questionSaveData.content())
                .createdAt(LocalDateTime.now()).build();

        questionRepository.save(question);
    }

    @Transactional
    public void saveAnswer(Long id, String answer) {
        Question question = questionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        question.updateAnswer(answer);
    }
}
