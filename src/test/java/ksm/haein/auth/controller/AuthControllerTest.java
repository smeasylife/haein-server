package ksm.haein.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksm.haein.auth.dto.KakaoAuthcode;
import ksm.haein.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@DisplayName("인증 컨트롤러 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("카카오 로그인 - 성공")
    void testKakaoLoginSuccess() throws Exception {
        // Given
        String code = "test authorization code";
        KakaoAuthcode kakaoAuthcode = new KakaoAuthcode(code);
        doNothing().when(authService).doKakaoLogin(eq(code), any());

        // When & Then
        mockMvc.perform(post("/auth/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kakaoAuthcode)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    @DisplayName("카카오 로그인 - 실패 (잘못된 코드)")
    void testKakaoLoginFailure() throws Exception {
        // Given
        String code = "invalid code";
        KakaoAuthcode kakaoAuthcode = new KakaoAuthcode(code);
        doThrow(new RuntimeException("Kakao login failed"))
                .when(authService).doKakaoLogin(eq(code), any());

        // When & Then
        mockMvc.perform(post("/auth/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kakaoAuthcode)))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("카카오 로그인 - 실패 (코드 누락)")
    void testKakaoLoginMissingCode() throws Exception {
        // Given
        KakaoAuthcode kakaoAuthcode = new KakaoAuthcode(null);

        // When & Then
        mockMvc.perform(post("/auth/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kakaoAuthcode)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
