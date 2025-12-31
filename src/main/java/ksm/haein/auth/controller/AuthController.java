package ksm.haein.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import ksm.haein.auth.dto.KakaoAuthcode;
import ksm.haein.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakao/login")
    public ResponseEntity<String> getAuthCode(KakaoAuthcode kakaoAuthcode, HttpServletRequest request){
        authService.doKakaoLogin(kakaoAuthcode.code(), request);
        return ResponseEntity.ok().body("Login successful");
    }
}
