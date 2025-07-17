package ksm.haein.auth.dto;

public record KakaoAuthcode(String authcode) {
    public static KakaoAuthcode of(String authcode) {
        return new KakaoAuthcode(authcode);
    }
}
