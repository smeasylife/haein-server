package ksm.haein.user.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailUtils {
    private final JavaMailSender javaMailSender;

    public void sendMailMessage(String to) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("헤이블 회원가입 인증 번호 메일입니다.");
            simpleMailMessage.setText("인증번호는 [" + generateRandomCode() + "] 입니다.");
            javaMailSender.send(simpleMailMessage);

            log.info("메일 발송 성공!");
        } catch (Exception e) {
            log.info("메일 발송 실패!");
            throw new RuntimeException(e);
        }
    }

    private String generateRandomCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
