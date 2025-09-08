package ksm.haein.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ksm.haein.config.security.login.CustomUser;
import ksm.haein.review.repository.ReviewRepository;
import ksm.haein.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 추가", description = "상품에 리뷰를 추가합니다.")
    @PostMapping("/{itemId}/review")
    public ResponseEntity<Void> addReview(@PathVariable Long itemId, @AuthenticationPrincipal CustomUser customUser, @RequestBody String content) {
        reviewService.addReview(content, itemId, customUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
