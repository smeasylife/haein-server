package ksm.haein.review.service;

import ksm.haein.review.entity.Review;
import ksm.haein.review.entity.ReviewComment;
import ksm.haein.review.repository.ReviewCommentRepository;
import ksm.haein.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommentService {
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;

    public void updateReviewComment(long reviewId, String comment) {
        Review review = reviewRepository.getReferenceById(reviewId);

        ReviewComment reviewComment = ReviewComment.builder()
                .review(review)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();

        reviewCommentRepository.save(reviewComment);
    }
}
