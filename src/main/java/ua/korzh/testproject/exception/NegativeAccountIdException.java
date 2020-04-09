package ua.korzh.testproject.exception;

public class NegativeAccountIdException extends RuntimeException {

    public NegativeAccountIdException(String message) {
        super(message);
    }
}
