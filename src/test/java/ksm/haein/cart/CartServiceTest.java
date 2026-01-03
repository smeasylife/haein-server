package ksm.haein.cart;

import ksm.haein.cart.entity.Cart;
import ksm.haein.cart.exceptions.CartAlreadyExistsException;
import ksm.haein.cart.repository.CartRepository;
import ksm.haein.cart.service.CartService;
import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.user.entity.Member;
import ksm.haein.user.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("장바구니 서비스 테스트")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("장바구니 담기 - 성공")
    void testAddCartSuccess() {
        // Given
        long memberId = 1L;
        long itemId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);
        Item mockItem = mock(Item.class);
        when(mockItem.getId()).thenReturn(itemId);

        when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(memberRepository.getReferenceById(memberId)).thenReturn(mockMember);
        when(itemRepository.getReferenceById(itemId)).thenReturn(mockItem);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        cartService.addCart(memberId, itemId);

        // Then
        verify(cartRepository).findByMemberId(memberId);
        verify(memberRepository).getReferenceById(memberId);
        verify(itemRepository).getReferenceById(itemId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 담기 - 실패 (이미 담긴 상품)")
    void testAddCartAlreadyExists() {
        // Given
        long memberId = 1L;
        long itemId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(memberId);
        Item mockItem = mock(Item.class);
        when(mockItem.getId()).thenReturn(itemId);

        Cart existingCart = Cart.builder()
                .member(mockMember)
                .item(mockItem)
                .build();

        when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.of(existingCart));

        // When & Then
        assertThrows(CartAlreadyExistsException.class, () -> cartService.addCart(memberId, itemId));
        verify(cartRepository).findByMemberId(memberId);
        verify(memberRepository, never()).getReferenceById(anyLong());
        verify(itemRepository, never()).getReferenceById(anyLong());
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 담기 - 실패 (존재하지 않는 회원)")
    void testAddCartMemberNotFound() {
        // Given
        long memberId = 999L;
        long itemId = 1L;

        when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(memberRepository.getReferenceById(memberId)).thenThrow(new jakarta.persistence.EntityNotFoundException("Member not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> cartService.addCart(memberId, itemId));
        verify(memberRepository).getReferenceById(memberId);
    }

    @Test
    @DisplayName("장바구니 담기 - 실패 (존재하지 않는 상품)")
    void testAddCartItemNotFound() {
        // Given
        long memberId = 1L;
        long itemId = 999L;

        Member mockMember = Member.builder().id(memberId).build();

        when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(memberRepository.getReferenceById(memberId)).thenReturn(mockMember);
        when(itemRepository.getReferenceById(itemId)).thenThrow(new jakarta.persistence.EntityNotFoundException("Item not found"));

        // When & Then
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> cartService.addCart(memberId, itemId));
        verify(itemRepository).getReferenceById(itemId);
    }
}
