package ksm.haein.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ksm.haein.auth.dto.KakaoUserInfo;
import ksm.haein.auth.utils.KakaoUtils;
import ksm.haein.config.security.login.CustomUser;
import ksm.haein.user.dto.MemberLoginData;
import ksm.haein.user.entity.Member;
import ksm.haein.user.enums.IdentityProvider;
import ksm.haein.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtils kakaoUtils;
    private final MemberService memberService;

    public void doKakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = kakaoUtils.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoUtils.getUserInfo(accessToken);

        Member member = memberService.getUserIfNotExistsSignup(userInfo.getEmail(), userInfo.getNickname());

        HttpSession session = request.getSession(true);
        request.changeSessionId();

        UserDetails userDetails = new CustomUser(makeKakaoMemberLoginData(member));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private MemberLoginData makeKakaoMemberLoginData(Member member) {
        return new MemberLoginData(
                member.getId(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getRole(),
                member.getCreatedAt(),
                null,
                IdentityProvider.KAKAO,
                null);
    }

}
