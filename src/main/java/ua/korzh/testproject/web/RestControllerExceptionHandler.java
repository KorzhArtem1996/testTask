package ua.korzh.testproject.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.korzh.testproject.exception.*;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({AccountNotExistException.class, NegativeAccountIdException.class, NegativeSumException.class, NotEnoughMoneyException.class})
    public ResponseEntity<Object> handleAccountNotExist(RuntimeException re, WebRequest request) {
        return handleExceptionInternal(re, re.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Object> handleEmailExists(RuntimeException re, WebRequest request) {
        return handleExceptionInternal(re, re.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
