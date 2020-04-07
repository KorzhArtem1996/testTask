package ua.korzh.testproject.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import ua.korzh.testproject.exception.AccountNotExistException;
import ua.korzh.testproject.exception.EmailExistsException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestControllerExceptionHandlerTest {

    private RestControllerExceptionHandler exceptionHandler = new RestControllerExceptionHandler();

    @Test
    void handleAccountNotExist() {
        ResponseEntity<Object> accountNotExist = exceptionHandler.handleAccountNotExistOrNegativeAccountIdOrNegativeSumOrNotEnoughMoney(new AccountNotExistException("Account not exist"), null);
        assertEquals("<400 BAD_REQUEST Bad Request,Account not exist,[]>", accountNotExist.toString());
    }

    @Test
    void handleEmailExists() {
        ResponseEntity<Object> emailExists = exceptionHandler.handleEmailExists(new EmailExistsException("Email already exists"), null);
        assertEquals("<409 CONFLICT Conflict,Email already exists,[]>", emailExists.toString());
    }
}