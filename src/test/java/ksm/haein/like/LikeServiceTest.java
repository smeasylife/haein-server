package ksm.haein.like;

import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.like.entity.Like;
import ksm.haein.like.repository.LikeRepository;
import ksm.haein.like.service.LikeService;
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
@DisplayName("좋아요 서비스 테스트")
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    @DisplayName("좋아요 추가 - 성공")
    void testAddLikeSuccess() {
        // Given
        Long memberId = 1L;
        Long itemId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);
        Item mockItem = mock(Item.class);
        when(mockItem.getId()).thenReturn(itemId);

        when(memberRepository.getReferenceById(memberId)).thenReturn(mockMember);
        when(itemRepository.getReferenceById(itemId)).thenReturn(mockItem);
        when(likeRepository.save(any(Like.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        likeService.addLike(itemId, memberId);

        // Then
        verify(memberRepository).getReferenceById(memberId);
        verify(itemRepository).getReferenceById(itemId);
        verify(likeRepository).save(any(Like.class));
    }

    @Test
    @DisplayName("좋아요 추가 - 실패 (존재하지 않는 회원)")
    void testAddLikeMemberNotFound() {
        // Given
        Long memberId = 999L;
        Long itemId = 1L;

        when(memberRepository.getReferenceById(memberId))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Member not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> likeService.addLike(itemId, memberId));
        verify(memberRepository).getReferenceById(memberId);
        verify(itemRepository, never()).getReferenceById(any());
        verify(likeRepository, never()).save(any());
    }

    @Test
    @DisplayName("좋아요 추가 - 실패 (존재하지 않는 상품)")
    void testAddLikeItemNotFound() {
        // Given
        Long memberId = 1L;
        Long itemId = 999L;

        Member mockMember = Member.builder().id(memberId).build();

        when(memberRepository.getReferenceById(memberId)).thenReturn(mockMember);
        when(itemRepository.getReferenceById(itemId))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Item not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class,
                () -> likeService.addLike(itemId, memberId));
        verify(itemRepository).getReferenceById(itemId);
        verify(likeRepository, never()).save(any());
    }
}
