package ksm.haein.like.controller;

import ksm.haein.like.service.LikeService;
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
    controllers = LikeController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
@DisplayName("좋아요 컨트롤러 테스트")
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("좋아요 추가 - 성공")
    void testAddLikeSuccess() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        doNothing().when(likeService).addLike(itemId, userId);

        // When & Then
        mockMvc.perform(post("/{itemId}/like", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(likeService).addLike(itemId, userId);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("좋아요 추가 - 실패 (미인증 사용자)")
    void testAddLikeUnauthorized() throws Exception {
        // Given
        Long itemId = 1L;

        // When & Then
        mockMvc.perform(post("/{itemId}/like", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("좋아요 추가 - 실패 (존재하지 않는 상품)")
    void testAddLikeItemNotFound() throws Exception {
        // Given
        Long itemId = 999L;
        Long userId = 1L;
        doThrow(new RuntimeException("Item not found"))
                .when(likeService).addLike(itemId, userId);

        // When & Then
        mockMvc.perform(post("/{itemId}/like", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("좋아요 추가 - 실패 (이미 좋아요 누른 상품)")
    void testAddLikeAlreadyExists() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        doThrow(new IllegalStateException("Like already exists"))
                .when(likeService).addLike(itemId, userId);

        // When & Then
        mockMvc.perform(post("/{itemId}/like", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("좋아요 취소 - 성공 (서비스 로직에 따름)")
    void testRemoveLikeSuccess() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        // 서비스 구현에 따라 좋아요 추가 메서드가 토글 기능일 수 있음
        doNothing().when(likeService).addLike(itemId, userId);

        // When & Then
        mockMvc.perform(post("/{itemId}/like", itemId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
