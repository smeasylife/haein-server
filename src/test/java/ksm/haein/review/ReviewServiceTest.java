package ksm.haein.review;

import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.review.entity.Review;
import ksm.haein.review.repository.ReviewRepository;
import ksm.haein.review.service.ReviewService;
import ksm.haein.user.entity.Member;
import ksm.haein.user.repository.MemberRepository;
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
@DisplayName("리뷰 서비스 테스트")
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("리뷰 추가 - 성공")
    void testAddReviewSuccess() {
        // Given
        String content = "좋은 상품입니다!";
        long itemId = 1L;
        long memberId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);
        Item mockItem = mock(Item.class);
        when(mockItem.getId()).thenReturn(itemId);

        when(memberRepository.getReferenceById(memberId)).thenReturn(mockMember);
        when(itemRepository.getReferenceById(itemId)).thenReturn(mockItem);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        reviewService.addReview(content, itemId, memberId);

        // Then
        verify(memberRepository).getReferenceById(memberId);
        verify(itemRepository).getReferenceById(itemId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 추가 - 실패 (존재하지 않는 회원)")
    void testAddReviewMemberNotFound() {
        // Given
        String content = "좋은 상품입니다!";
        long itemId = 1L;
        long memberId = 999L;

        when(memberRepository.getReferenceById(memberId))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Member not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> reviewService.addReview(content, itemId, memberId));
        verify(memberRepository).getReferenceById(memberId);
        verify(itemRepository, never()).getReferenceById(any());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("리뷰 추가 - 실패 (존재하지 않는 상품)")
    void testAddReviewItemNotFound() {
        // Given
        String content = "좋은 상품입니다!";
        long itemId = 999L;
        long memberId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);

        when(memberRepository.getReferenceById(memberId)).thenReturn(mockMember);
        when(itemRepository.getReferenceById(itemId))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Item not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> reviewService.addReview(content, itemId, memberId));
        verify(itemRepository).getReferenceById(itemId);
        verify(reviewRepository, never()).save(any());
    }
}
