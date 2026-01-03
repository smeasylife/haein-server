package ksm.haein.auth;

import jakarta.servlet.http.HttpServletRequest;
import ksm.haein.auth.dto.KakaoUserInfo;
import ksm.haein.auth.service.AuthService;
import ksm.haein.auth.utils.KakaoUtils;
import ksm.haein.user.entity.Member;
import ksm.haein.user.enums.Role;
import ksm.haein.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 서비스 테스트")
class AuthServiceTest {

    @Mock
    private KakaoUtils kakaoUtils;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("카카오 로그인 - 신규 회원")
    void testDoKakaoLoginNewMember() {
        // Given
        String code = "test_code";
        String accessToken = "test_access_token";
        String email = "test@example.com";
        String nickname = "testuser";

        MockHttpServletRequest request = new MockHttpServletRequest();

        Member mockMember = Member.builder()
                .id(1L)
                .email(email)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();

        when(kakaoUtils.getAccessToken(code)).thenReturn(accessToken);
        when(kakaoUtils.getUserInfo(accessToken)).thenReturn(createMockKakaoUserInfo(email, nickname));
        when(memberService.getUserIfNotExistsSignup(email, nickname)).thenReturn(mockMember);

        // When
        authService.doKakaoLogin(code, request);

        // Then
        verify(kakaoUtils).getAccessToken(code);
        verify(kakaoUtils).getUserInfo(accessToken);
        verify(memberService).getUserIfNotExistsSignup(email, nickname);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    @DisplayName("카카오 로그인 - 기존 회원")
    void testDoKakaoLoginExistingMember() {
        // Given
        String code = "test_code";
        String accessToken = "test_access_token";
        String email = "existing@example.com";
        String nickname = "existinguser";

        MockHttpServletRequest request = new MockHttpServletRequest();

        Member mockMember = Member.builder()
                .id(2L)
                .email(email)
                .nickname(nickname)
                .role(Role.ROLE_USER)
                .build();

        when(kakaoUtils.getAccessToken(code)).thenReturn(accessToken);
        when(kakaoUtils.getUserInfo(accessToken)).thenReturn(createMockKakaoUserInfo(email, nickname));
        when(memberService.getUserIfNotExistsSignup(email, nickname)).thenReturn(mockMember);

        // When
        authService.doKakaoLogin(code, request);

        // Then
        verify(memberService).getUserIfNotExistsSignup(email, nickname);
        assertNotNull(request.getSession(false));
    }

    @Test
    @DisplayName("카카오 로그인 - 액세스 토큰获取 실패")
    void testDoKakaoLoginGetAccessTokenFailure() {
        // Given
        String code = "invalid_code";
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(kakaoUtils.getAccessToken(code)).thenThrow(new RuntimeException("Failed to get access token"));

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.doKakaoLogin(code, request));
        verify(kakaoUtils).getAccessToken(code);
        verify(kakaoUtils, never()).getUserInfo(anyString());
    }

    @Test
    @DisplayName("카카오 로그인 - 사용자 정보获取 실패")
    void testDoKakaoLoginGetUserInfoFailure() {
        // Given
        String code = "test_code";
        String accessToken = "test_access_token";
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(kakaoUtils.getAccessToken(code)).thenReturn(accessToken);
        when(kakaoUtils.getUserInfo(accessToken)).thenThrow(new RuntimeException("Failed to get user info"));

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.doKakaoLogin(code, request));
        verify(kakaoUtils).getAccessToken(code);
        verify(kakaoUtils).getUserInfo(accessToken);
        verify(memberService, never()).getUserIfNotExistsSignup(anyString(), anyString());
    }

    private KakaoUserInfo createMockKakaoUserInfo(String email, String nickname) {
        return new KakaoUserInfo(
                123456L,
                new KakaoUserInfo.KakaoAccount(email, new KakaoUserInfo.Profile(nickname)),
                new KakaoUserInfo.Properties(nickname)
        );
    }
}
