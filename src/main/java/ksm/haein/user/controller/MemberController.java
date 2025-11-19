package ksm.haein.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ksm.haein.config.redis.RedisService;
import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.dto.UserVerificationCode;
import ksm.haein.user.service.MailService;
import ksm.haein.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;
    private final RedisService redisService;

    @Operation(summary = "로컬 회원가입", description = "로컬 계정으로 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequestForm signUpRequestForm) {
        memberService.localSignup(signUpRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "회원가입 인증 코드 전송", description = "회원가입을 위한 인증 코드를 이메일로 전송합니다.")
    @PostMapping("/signup/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        mailService.sendMailMessage(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("인증 번호 전송 성공");
    }

    @Operation(summary = "회원가입 인증 코드 확인", description = "전송된 인증 코드를 확인합니다.")
    @PostMapping("/signup/verify-code")
    public ResponseEntity<String> verifyUserCode(@RequestBody UserVerificationCode userVerificationCode) {
        if (redisService.compareVerificationCode(userVerificationCode.email(), userVerificationCode.code())) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 번호 매치 실패");
        }
    }

}
