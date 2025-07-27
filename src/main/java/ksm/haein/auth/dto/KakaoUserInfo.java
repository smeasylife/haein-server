package ksm.haein.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoUserInfo(
        Long id,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount,
        Properties properties
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record KakaoAccount(String email, Profile profile) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Profile(String nickname) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Properties(String nickname) {}

    public String getEmail() {
        return kakaoAccount.email();
    }

    public String getNickname() {
        return properties.nickname();
    }
}
