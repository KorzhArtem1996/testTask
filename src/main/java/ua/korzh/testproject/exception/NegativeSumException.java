package ua.korzh.testproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegativeSumException extends RuntimeException {
    public NegativeSumException(String message) {
        super(message);
    }
}
