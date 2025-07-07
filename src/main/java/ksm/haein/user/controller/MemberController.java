package ksm.haein.user.controller;

import jakarta.validation.Valid;
import ksm.haein.user.dto.SignUpRequestForm;
import ksm.haein.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestForm signUpRequestForm) {
        memberService.saveMember(signUpRequestForm);
        return ResponseEntity.ok("회원가입 성공");
    }
}
