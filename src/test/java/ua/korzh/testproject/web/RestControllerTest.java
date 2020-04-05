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
import ua.korzh.testproject.exception.EmailExistsException;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestController.class)
public class RestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;

    @Test
    public void getAllClientsTest() throws Exception {
        List<Client> list = Stream
                .of(new Client("client1", "password1"), new Client("client2", "password2"), new Client("client3", "password3"))
                .collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        given(clientService.getAll()).willReturn(list);
        this.mockMvc.perform(
                get("/clients").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(list)));
    }

    @Test
    public void registerClientTest() throws Exception{
        Client client = new Client("email", "password");
        ObjectMapper mapper = new ObjectMapper();

        given(clientService.register(anyString(), anyString())).willReturn(client).willThrow(EmailExistsException.class);
        this.mockMvc.perform(
                post("/clients?email=email&password=password").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(mapper.writeValueAsString(client)));
        this.mockMvc.perform(
                post("/clients?email=email&password=password").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(mapper.writeValueAsString(null)));
    }

    @Test
    public void depositMoneyTest() throws Exception {
        Client client = new Client("deposit", "money");
        ObjectMapper mapper = new ObjectMapper();
        Account account1 = new Account();
        account1.setBalance(100L);
        Account account2 = new Account();
        account2.setBalance(200L);
        given(clientService.deposit(anyLong(), anyInt())).willReturn(account1, account2);
        given(clientService.getById(anyInt())).willReturn(client);

        this.mockMvc.perform(
                put("/clients/1/accounts/1/deposit?money=500").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(account1)));
        this.mockMvc.perform(
                put("/clients/1/accounts/1/deposit?money=500").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(account2)));
    }

    @Test
    public void withdrawMoneyTest() throws Exception {
        Client client = new Client("withdraw", "money");
        ObjectMapper mapper = new ObjectMapper();
        Account account = new Account();
        account.setBalance(100L);
        given(clientService.withdraw( anyLong(), anyInt())).willReturn(account);
        given(clientService.getById(anyInt())).willReturn(client);

        this.mockMvc.perform(
                put("/clients/5/accounts/1/withdraw?sum=100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(account)));
    }

    @Test
    public void checkBalanceTest() throws Exception {
        Client client = new Client("check", "balance");
        client.addAccount(new Account());
        ObjectMapper mapper = new ObjectMapper();
        given(clientService.checkBalance(anyInt())).willReturn(500L);
        given(clientService.getById(anyInt())).willReturn(client);

        this.mockMvc.perform(
                get("/clients/3/accounts/1/balance").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(500L)));
    }

    @Test
    public void showTransactionTest() throws Exception {
        Client client = new Client("show", "history");
        ObjectMapper mapper = new ObjectMapper();
        List<Transaction> list = Arrays.asList(new Transaction(OperationName.DEPOSIT, LocalDateTime.now()), new Transaction(OperationName.WITHDRAW, LocalDateTime.now()), new Transaction(OperationName.DEPOSIT, LocalDateTime.now()));
        given(clientService.showTransaction(anyInt())).willReturn(list);

        this.mockMvc.perform(get("/clients/1/accounts/1/history").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
