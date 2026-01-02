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
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
        try {
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
                throw new KakaoAccessTokenRequestException("카카오 로그인 과정 중에 문제가 발생했습니다. (카카오 엑세스 토큰 문제)");
            }

            return tokenResponse.accessToken();
        } catch (WebClientResponseException e) {
            throw new KakaoAccessTokenRequestException("카카오 토큰 요청 실패: " + e.getMessage());
        } catch (Exception e) {
            throw new KakaoAccessTokenRequestException("카카오 토큰 요청 중 오류 발생: " + e.getMessage());
        }
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        try {
            KakaoUserInfo userInfo = webClient.get()
                    .uri(userInfoUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8")
                    .retrieve()
                    .bodyToMono(KakaoUserInfo.class)
                    .block();

            if (userInfo == null) {
                throw new KakaoAccessTokenRequestException("사용자 정보를 가져올 수 없습니다");
            }

            return userInfo;
        } catch (WebClientResponseException e) {
            throw new KakaoAccessTokenRequestException("카카오 사용자 정보 조회 실패: " + e.getMessage());
        } catch (Exception e) {
            throw new KakaoAccessTokenRequestException("카카오 사용자 정보 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
