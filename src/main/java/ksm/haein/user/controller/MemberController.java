package ksm.haein.user.controller;

import jakarta.validation.Valid;
import ksm.haein.user.dto.MemberData;
import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.dto.UserVerificationCode;
import ksm.haein.user.service.MailService;
import ksm.haein.user.service.MemberService;
import ksm.haein.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;
    private final RedisService redisService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequestForm signUpRequestForm) {
        memberService.localSignup(signUpRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signup/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        mailService.sendMailMessage(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("인증 번호 전송 성공");
    }

    @PostMapping("/signup/verify-code")
    public ResponseEntity<String> verifyUserCode(@RequestBody UserVerificationCode userVerificationCode) {
        if (redisService.compareVerificationCode(userVerificationCode.email(), userVerificationCode.code())) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 번호 매치 실패");
        }
    }

    @GetMapping("/members")
    public ResponseEntity<Page<MemberData>> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MemberData> members = memberService.getMembersByPage(pageRequest);
        return ResponseEntity.ok(members);
    }

}
