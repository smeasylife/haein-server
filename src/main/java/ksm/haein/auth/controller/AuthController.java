package ksm.haein.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "인증", description = "인증 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 이용하여 로그인합니다.")
    @PostMapping("/kakao/login")
    public ResponseEntity<String> getAuthCode(@RequestBody KakaoAuthcode kakaoAuthcode,
                                              HttpServletRequest request,
                                              HttpServletResponse response){
        authService.doKakaoLogin(kakaoAuthcode.authcode(), request ,response);
        return ResponseEntity.ok().body("Login successful");
    }
}
