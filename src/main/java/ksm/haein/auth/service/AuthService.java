package ksm.haein.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ksm.haein.auth.dto.KakaoUserInfo;
import ksm.haein.auth.utils.KakaoUtils;
import ksm.haein.user.entity.Member;
import ksm.haein.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtils kakaoUtils;
    private final MemberService memberService;
    private static final String HTTP_SESSION_KEY

    public void doKakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = kakaoUtils.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoUtils.getUserInfo(accessToken);

        Member member = memberService.getUserIfNotExistsSignup(userInfo.getEmail(), userInfo.getNickname());

        HttpSession session = request.getSession(true);
        session.setAttribute("loginMember", member);
        session.setMaxInactiveInterval(30 * 60);
    }


}
