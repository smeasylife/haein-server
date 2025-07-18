package ksm.haein.auth.utils;

import ksm.haein.auth.dto.KakaoTokenResponse;
import ksm.haein.auth.dto.KakaoUserInfo;
import ksm.haein.auth.exception.KakaoAccessTokenRequestException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Getter
public class KakaoUtils {
    @Value("${kakao.auth.client}")
    private String clientId;
    @Value("${kakao.auth.redirect}")
    private String redirectUri;
    @Value("${kakao.auth.token-uri}")
    private String tokenUri;
    @Value("${kakao.auth.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient = WebClient.builder().build();


    public String getAccessToken(String code) {
        KakaoTokenResponse tokenResponse = webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        if (tokenResponse == null || tokenResponse.accessToken() == null) {
            throw new KakaoAccessTokenRequestException("카카로 로그인 과정 중에 문제가 발생했습니다. (카카오 엑세스 토큰 문제)");
        }

        return tokenResponse.accessToken();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return webClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }
}
