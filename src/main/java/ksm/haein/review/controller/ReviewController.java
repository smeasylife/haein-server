package ksm.haein.review.controller;

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

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{itemId}/review")
    public ResponseEntity<Void> addReview(@PathVariable Long itemId, @AuthenticationPrincipal CustomUser customUser, @RequestBody String content) {
        reviewService.addReview(content, itemId, customUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
