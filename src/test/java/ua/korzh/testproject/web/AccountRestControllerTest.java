package ua.korzh.testproject.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.OperationName;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.service.account.AccountService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Replace import org.junit.jupiter.api.Test; to import org.junit.Test;

@WebMvcTest(AccountRestController.class)
public class AccountRestControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @Test
    public void depositMoneyTest() throws Exception {
        Account account = new Account();
        account.setBalance(100L);
        long money = 500L;
        int clientId = 1;
        int accountId = 1;
        given(accountService.deposit(eq(money), eq(accountId))).willReturn(account);

        ResultActions resultActions = this.mockMvc.perform(
                put("/clients/{clientId}/accounts/{accountId}/deposit?money={money}",
                        clientId, accountId, money));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(account)));
    }

    @Test
    public void withdrawMoneyTest() throws Exception {
        Account account = new Account();
        account.setBalance(100L);
        long sum = 100L;
        int clientId = 1;
        int accountId = 1;
        given(accountService.withdraw(eq(sum), eq(accountId))).willReturn(account);

        ResultActions resultActions = this.mockMvc.perform(
                put("/clients/{clientI}/accounts/{accountId}/withdraw?sum={sum}",
                        clientId, accountId, sum));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(account)));
    }

    @Test
    public void checkBalanceTest() throws Exception {
        int clientId = 1;
        int accountId = 1;
        long balance = 500L;
        given(accountService.checkBalance(eq(accountId))).willReturn(balance);

        ResultActions resultActions = this.mockMvc.perform(
                get("/clients/{clientId}/accounts/{accountId}/balance",
                        clientId, accountId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(balance)));
    }

    @Test
    public void showTransactionTest() throws Exception {
        List<Transaction> list = Arrays.asList(new Transaction(OperationName.DEPOSIT, LocalDateTime.now()),
                new Transaction(OperationName.WITHDRAW, LocalDateTime.now()),
                new Transaction(OperationName.DEPOSIT, LocalDateTime.now()));
        int clientId = 1;
        int accountId = 1;
        given(accountService.history(eq(accountId))).willReturn(list);

        ResultActions resultActions = this.mockMvc.perform(
                get("/clients/{clientId}/accounts/{accountId}/history",
                        clientId, accountId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
