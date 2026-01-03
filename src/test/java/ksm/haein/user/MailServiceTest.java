package ksm.haein.user;

import ksm.haein.user.exception.MailSendException;
import ksm.haein.user.service.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메일 서비스 테스트")
class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("인증 메일 전송 - 성공")
    void testSendMailMessageSuccess() {
        // Given
        String to = "test@example.com";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), anyString(), any());

        // When
        mailService.sendMailMessage(to);

        // Then
        verify(javaMailSender).send(any(SimpleMailMessage.class));
        verify(valueOperations).set(eq(to), anyString(), any());
    }

    @Test
    @DisplayName("인증 메일 전송 - 실패 (메일 전송 실패)")
    void testSendMailMessageFailure() {
        // Given
        String to = "test@example.com";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), anyString(), any());
        doThrow(new RuntimeException("Mail send failed")).when(javaMailSender).send(any(SimpleMailMessage.class));

        // When & Then
        assertThrows(MailSendException.class, () -> mailService.sendMailMessage(to));
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("인증 메일 전송 - 실패 (Redis 저장 실패)")
    void testSendMailMessageRedisFailure() {
        // Given
        String to = "test@example.com";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doThrow(new RuntimeException("Redis connection failed")).when(valueOperations).set(anyString(), anyString(), any());

        // When & Then
        assertThrows(RuntimeException.class, () -> mailService.sendMailMessage(to));
    }

    @Test
    @DisplayName("인증 코드 형식 확인")
    void testVerificationCodeFormat() {
        // Given
        String to = "test@example.com";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doAnswer(invocation -> {
            String code = invocation.getArgument(1);
            // 인증 코드가 6자리 숫자인지 확인
            assertTrue(code.matches("\\d{6}"));
            int codeValue = Integer.parseInt(code);
            assertTrue(codeValue >= 100000 && codeValue <= 999999);
            return null;
        }).when(valueOperations).set(anyString(), anyString(), any());

        // When
        mailService.sendMailMessage(to);

        // Then
        verify(redisTemplate.opsForValue()).set(eq(to), anyString(), any());
    }
}
