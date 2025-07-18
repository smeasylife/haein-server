package ksm.haein.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import ksm.haein.auth.dto.KakaoUserInfo;
import ksm.haein.auth.utils.KakaoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtils kakaoUtils;

    public void doKakaoLogin(String code, HttpServletResponse response) {
        String accessToken = kakaoUtils.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoUtils.getUserInfo(accessToken);


    }
}
