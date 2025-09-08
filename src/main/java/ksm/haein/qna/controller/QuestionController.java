package ksm.haein.qna.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ksm.haein.qna.dto.QuestionSaveData;
import ksm.haein.qna.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Q&A", description = "Q&A 관련 API")
@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @Operation(summary = "Q&A 질문 등록", description = "Q&A 질문을 등록합니다.")
    @PostMapping("/question")
    public ResponseEntity<Void> saveQuestion(@RequestBody QuestionSaveData questionSaveData) {
        questionService.save(questionSaveData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
