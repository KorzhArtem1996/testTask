package ua.korzh.testproject.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import ua.korzh.testproject.exception.AccountNotExistException;
import ua.korzh.testproject.exception.EmailExistsException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRestControllerExceptionHandlerTest {

    private RestControllerExceptionHandler exceptionHandler = new RestControllerExceptionHandler();

    @Test
    void handleAccountNotExist() {
        ResponseEntity<Object> accountNotExist =
                exceptionHandler.handleAccountNotExistOrNegativeAccountIdOrNegativeSumOrNotEnoughMoney(
                        new AccountNotExistException("Account not exist"), null);

        assertEquals(HttpStatus.BAD_REQUEST, accountNotExist.getStatusCode());
        assertEquals(400, accountNotExist.getStatusCodeValue());

    }

    @Test
    void handleEmailExists() {
        ResponseEntity<Object> emailExists = exceptionHandler.handleEmailExists(
                new EmailExistsException("Email already exists"), null);

        assertEquals(HttpStatus.CONFLICT, emailExists.getStatusCode());
        assertEquals(409, emailExists.getStatusCodeValue());
    }

}