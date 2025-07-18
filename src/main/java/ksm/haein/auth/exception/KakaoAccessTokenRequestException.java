package ksm.haein.auth.exception;

public class KakaoAccessTokenRequestException extends RuntimeException {
    public KakaoAccessTokenRequestException(String message) {
        super(message);
    }
}
