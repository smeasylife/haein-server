package ksm.haein.cart.controller;

import ksm.haein.cart.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = CartController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
@DisplayName("장바구니 컨트롤러 테스트")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("장바구니 담기 - 성공")
    void testAddCartSuccess() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        doNothing().when(cartService).addCart(userId, itemId);

        // When & Then
        mockMvc.perform(post("/{itemId}/cart", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(cartService).addCart(userId, itemId);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("장바구니 담기 - 실패 (미인증 사용자)")
    void testAddCartUnauthorized() throws Exception {
        // Given
        Long itemId = 1L;

        // When & Then
        mockMvc.perform(post("/{itemId}/cart", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("장바구니 담기 - 실패 (존재하지 않는 상품)")
    void testAddCartItemNotFound() throws Exception {
        // Given
        Long itemId = 999L;
        Long userId = 1L;
        doThrow(new RuntimeException("Item not found"))
                .when(cartService).addCart(userId, itemId);

        // When & Then
        mockMvc.perform(post("/{itemId}/cart", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("장바구니 담기 - 실패 (이미 담긴 상품)")
    void testAddCartAlreadyExists() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        doThrow(new IllegalStateException("Item already in cart"))
                .when(cartService).addCart(userId, itemId);

        // When & Then
        mockMvc.perform(post("/{itemId}/cart", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
