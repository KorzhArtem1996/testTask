package ua.korzh.testproject.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.OperationName;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.service.client.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountRestController.class)
public class AccountRestControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;

    @Test
    public void depositMoneyTest() throws Exception {
        Client client = new Client("deposit", "money");
        Account account1 = new Account();
        account1.setBalance(100L);
        Account account2 = new Account();
        account2.setBalance(200L);
        given(clientService.deposit(anyLong(), anyInt())).willReturn(account1, account2);
        given(clientService.getById(anyInt())).willReturn(client);

        ResultActions resultActions = this.mockMvc.perform(
                put("/clients/1/accounts/1/deposit?money=500").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(account1)));

        resultActions = this.mockMvc.perform(
                put("/clients/1/accounts/1/deposit?money=500").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(account2)));
    }

    @Test
    public void withdrawMoneyTest() throws Exception {
        Client client = new Client("withdraw", "money");
        Account account = new Account();
        account.setBalance(100L);
        given(clientService.withdraw(anyLong(), anyInt())).willReturn(account);
        given(clientService.getById(anyInt())).willReturn(client);

        ResultActions resultActions = this.mockMvc.perform(
                put("/clients/5/accounts/1/withdraw?sum=100").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(account)));
    }

    @Test
    public void checkBalanceTest() throws Exception {
        Client client = new Client("check", "balance");
        client.addAccount(new Account());
        given(clientService.checkBalance(anyInt())).willReturn(500L);
        given(clientService.getById(anyInt())).willReturn(client);

        ResultActions resultActions = this.mockMvc.perform(
                get("/clients/3/accounts/1/balance").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(500L)));
    }

    @Test
    public void showTransactionTest() throws Exception {
        Client client = new Client("show", "history");
        List<Transaction> list = Arrays.asList(new Transaction(OperationName.DEPOSIT, LocalDateTime.now()),
                new Transaction(OperationName.WITHDRAW, LocalDateTime.now()),
                new Transaction(OperationName.DEPOSIT, LocalDateTime.now()));
        given(clientService.showTransaction(anyInt())).willReturn(list);

        ResultActions resultActions = this.mockMvc.perform(
                get("/clients/1/accounts/1/history").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
