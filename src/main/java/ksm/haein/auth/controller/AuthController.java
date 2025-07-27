package ksm.haein.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ksm.haein.auth.dto.KakaoAuthcode;
import ksm.haein.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/kakao/login")
    public ResponseEntity<String> getAuthCode(@RequestBody KakaoAuthcode kakaoAuthcode,
                                              HttpServletRequest request,
                                              HttpServletResponse response){
        authService.doKakaoLogin(kakaoAuthcode.authcode(), response);
        return ResponseEntity.ok().body("Login successful");
    }
}
