package ksm.haein.auth.dto;

public record KakaoAuthcode(String code, String error, String error_description, String state) {
    public static KakaoAuthcode of(String authcode,  String error, String error_description, String state) {
        return new KakaoAuthcode(authcode,  error, error_description, state);
    }
}
