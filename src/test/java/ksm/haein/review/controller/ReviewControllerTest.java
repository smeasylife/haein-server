package ksm.haein.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksm.haein.review.dto.ReviewCommentData;
import ksm.haein.review.service.ReviewCommentService;
import ksm.haein.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@DisplayName("리뷰 컨트롤러 테스트")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewCommentService reviewCommentService;

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("리뷰 작성 - 성공")
    void testAddReviewSuccess() throws Exception {
        // Given
        Long itemId = 1L;
        Long userId = 1L;
        String content = "좋은 상품입니다!";
        doNothing().when(reviewService).addReview(eq(content), eq(itemId), eq(userId));

        // When & Then
        mockMvc.perform(post("/{itemId}/review", itemId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(content)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(reviewService).addReview(eq(content), eq(itemId), eq(userId));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("리뷰 작성 - 실패 (미인증 사용자)")
    void testAddReviewUnauthorized() throws Exception {
        // Given
        Long itemId = 1L;
        String content = "좋은 상품입니다!";

        // When & Then
        mockMvc.perform(post("/{itemId}/review", itemId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(content)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("리뷰 작성 - 실패 (존재하지 않는 상품)")
    void testAddReviewItemNotFound() throws Exception {
        // Given
        Long itemId = 999L;
        Long userId = 1L;
        String content = "좋은 상품입니다!";
        doThrow(new RuntimeException("Item not found"))
                .when(reviewService).addReview(eq(content), eq(itemId), eq(userId));

        // When & Then
        mockMvc.perform(post("/{itemId}/review", itemId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(content)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("리뷰 작성 - 실패 (빈 내용)")
    void testAddReviewEmptyContent() throws Exception {
        // Given
        Long itemId = 1L;
        String content = "";

        // When & Then
        mockMvc.perform(post("/{itemId}/review", itemId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(content)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("리뷰 댓글 작성 - 성공")
    void testUpdateReviewCommentSuccess() throws Exception {
        // Given
        Long reviewId = 1L;
        ReviewCommentData commentData = new ReviewCommentData("답변입니다.");
        doNothing().when(reviewCommentService).updateReviewComment(reviewId, commentData.comment());

        // When & Then
        mockMvc.perform(post("/{reviewId}/comment", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(reviewCommentService).updateReviewComment(reviewId, commentData.comment());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("리뷰 댓글 작성 - 실패 (미인증 사용자)")
    void testUpdateReviewCommentUnauthorized() throws Exception {
        // Given
        Long reviewId = 1L;
        ReviewCommentData commentData = new ReviewCommentData("답변입니다.");

        // When & Then
        mockMvc.perform(post("/{reviewId}/comment", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("리뷰 댓글 작성 - 실패 (존재하지 않는 리뷰)")
    void testUpdateReviewCommentReviewNotFound() throws Exception {
        // Given
        Long reviewId = 999L;
        ReviewCommentData commentData = new ReviewCommentData("답변입니다.");
        doThrow(new RuntimeException("Review not found"))
                .when(reviewCommentService).updateReviewComment(reviewId, commentData.comment());

        // When & Then
        mockMvc.perform(post("/{reviewId}/comment", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentData))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
