package ksm.haein.qna.controller;

import ksm.haein.qna.dto.QuestionSaveData;
import ksm.haein.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/question")
    public ResponseEntity<Void> saveQuestion(@RequestBody QuestionSaveData questionSaveData) {
        questionService.save(questionSaveData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
