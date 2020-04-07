package ua.korzh.testproject.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @ExceptionHandler({AccountNotExistException.class, NegativeAccountIdException.class,
            NegativeSumException.class, NotEnoughMoneyException.class})

    public ResponseEntity<Object> handleAccountNotExistOrNegativeAccountIdOrNegativeSumOrNotEnoughMoney(
            RuntimeException re, WebRequest request) {
        LOG.error(re.getMessage(), re);
        return handleExceptionInternal(re, re.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Object> handleEmailExists(RuntimeException re, WebRequest request) {
        LOG.error(re.getMessage(), re);
        return handleExceptionInternal(re, re.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception e, WebRequest request) {
        LOG.error(e.getMessage(), e);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
