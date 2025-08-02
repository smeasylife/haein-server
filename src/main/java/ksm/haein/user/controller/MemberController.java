package ksm.haein.user.controller;

import jakarta.validation.Valid;
import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.service.MailService;
import ksm.haein.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MailService mailUtils;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequestForm signUpRequestForm) {
        memberService.localLogin(signUpRequestForm);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signup/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        mailUtils.sendMailMessage(email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
