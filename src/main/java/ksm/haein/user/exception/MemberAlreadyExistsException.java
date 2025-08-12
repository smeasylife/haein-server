package ksm.haein.user.exception;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(String message) {
        super(message);
    }
    public MemberAlreadyExistsException() {}
}
