package ksm.haein.user.service;

import ksm.haein.config.mail.MyMailServiceConfig;
import ksm.haein.user.exception.MailSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(MyMailServiceConfig.class)
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;

    public void sendMailMessage(String to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("헤이블 회원가입 인증 번호 메일입니다.");
            simpleMailMessage.setText("인증번호는 [" + generateRandomCode(to) + "] 입니다.");
            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            throw new MailSendException("메일 발송 실패!");
        }
    }

    private String generateRandomCode(String email) {
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5));
        return code;
    }
}
