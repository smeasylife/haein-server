package ksm.haein.review;

import ksm.haein.review.entity.Review;
import ksm.haein.review.entity.ReviewComment;
import ksm.haein.review.repository.ReviewCommentRepository;
import ksm.haein.review.repository.ReviewRepository;
import ksm.haein.review.service.ReviewCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("리뷰 댓글 서비스 테스트")
class ReviewCommentServiceTest {

    @Mock
    private ReviewCommentRepository reviewCommentRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewCommentService reviewCommentService;

    @Test
    @DisplayName("리뷰 댓글 추가 - 성공")
    void testUpdateReviewCommentSuccess() {
        // Given
        long reviewId = 1L;
        String comment = "답변입니다.";

        Review mockReview = Review.builder().id(reviewId).build();

        when(reviewRepository.getReferenceById(reviewId)).thenReturn(mockReview);
        when(reviewCommentRepository.save(any(ReviewComment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        reviewCommentService.updateReviewComment(reviewId, comment);

        // Then
        verify(reviewRepository).getReferenceById(reviewId);
        verify(reviewCommentRepository).save(any(ReviewComment.class));
    }

    @Test
    @DisplayName("리뷰 댓글 추가 - 실패 (존재하지 않는 리뷰)")
    void testUpdateReviewCommentReviewNotFound() {
        // Given
        long reviewId = 999L;
        String comment = "답변입니다.";

        when(reviewRepository.getReferenceById(reviewId))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Review not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> reviewCommentService.updateReviewComment(reviewId, comment));
        verify(reviewRepository).getReferenceById(reviewId);
        verify(reviewCommentRepository, never()).save(any());
    }
}
