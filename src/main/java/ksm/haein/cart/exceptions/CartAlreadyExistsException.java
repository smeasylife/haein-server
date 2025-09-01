package ksm.haein.cart.exceptions;

public class CartAlreadyExistsException extends RuntimeException {
    public CartAlreadyExistsException() {
        super();
    }

    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
