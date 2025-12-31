package ksm.haein.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ksm.haein.config.redis.RedisService;
import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.dto.UserVerificationCode;
import ksm.haein.user.exception.MemberAlreadyExistsException;
import ksm.haein.user.service.MailService;
import ksm.haein.user.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@DisplayName("회원관리 컨트롤러 테스트")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MailService mailService;

    @MockBean
    private RedisService redisService;

    @Test
    @DisplayName("회원가입 - 성공")
    void testSignupSuccess() throws Exception {
        // Given
        SignUpRequestForm signUpRequestForm = new SignUpRequestForm(
                "testuser",
                "test@example.com",
                "password123!",
                "01012345678"
        );
        doNothing().when(memberService).localSignup(any(SignUpRequestForm.class));

        // When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestForm)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 - 실패 (이미 존재하는 회원)")
    void testSignupFailureMemberExists() throws Exception {
        // Given
        SignUpRequestForm signUpRequestForm = new SignUpRequestForm(
                "existinguser",
                "existing@example.com",
                "password123!",
                "01012345678"
        );
        doThrow(new MemberAlreadyExistsException("Member already exists"))
                .when(memberService).localSignup(any(SignUpRequestForm.class));

        // When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestForm)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("회원가입 - 실패 (유효성 검증 실패)")
    void testSignupFailureValidation() throws Exception {
        // Given
        SignUpRequestForm signUpRequestForm = new SignUpRequestForm(
                "",  // 빈 닉네임
                "invalid-email",  // 잘못된 이메일
                "123",  // 짧은 비밀번호
                ""  // 빈 전화번호
        );

        // When & Then
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestForm)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증 코드 전송 - 성공")
    void testSendVerificationCodeSuccess() throws Exception {
        // Given
        String email = "test@example.com";
        doNothing().when(mailService).sendMailMessage(email);

        // When & Then
        mockMvc.perform(post("/signup/send-code")
                        .param("email", email))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("인증 번호 전송 성공"));
    }

    @Test
    @DisplayName("인증 코드 전송 - 실패 (이메일 누락)")
    void testSendVerificationCodeMissingEmail() throws Exception {
        // When & Then
        mockMvc.perform(post("/signup/send-code"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증 코드 검증 - 성공")
    void testVerifyUserCodeSuccess() throws Exception {
        // Given
        String email = "test@example.com";
        String code = "123456";
        UserVerificationCode verificationCode = new UserVerificationCode(email, code);
        when(redisService.compareVerificationCode(email, code)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/signup/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationCode)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("인증 성공"));
    }

    @Test
    @DisplayName("인증 코드 검증 - 실패 (코드 불일치)")
    void testVerifyUserCodeFailure() throws Exception {
        // Given
        String email = "test@example.com";
        String code = "wrong_code";
        UserVerificationCode verificationCode = new UserVerificationCode(email, code);
        when(redisService.compareVerificationCode(email, code)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/signup/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationCode)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("인증 번호 매치 실패"));
    }
}
