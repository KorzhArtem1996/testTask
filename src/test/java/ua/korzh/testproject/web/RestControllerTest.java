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
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.service.client.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    @MockBean
    private DepositeService depositeService;
    @MockBean
    private SingUpService singUpService;
    @MockBean
    private WithdrawService withdrawService;
    @MockBean
    private CheckBalanceService checkBalanceService;

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

        given(singUpService.register(anyString(), anyString())).willReturn(client).willThrow(EmailExistsException.class);
        this.mockMvc.perform(
                post("/register?email=email&password=password").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(client)));
        this.mockMvc.perform(
                post("/register?email=email&password=password").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(null)));
    }

    @Test
    public void depositeMoneyTest() throws Exception {
        Client client = new Client("deposite", "money");
        ObjectMapper mapper = new ObjectMapper();
        given(depositeService.deposite(anyLong(), anyInt())).willReturn(false, true);
        given(clientService.getById(anyInt())).willReturn(client);

        this.mockMvc.perform(
                put("/clients/1/deposite?money=500&accountId=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(false)));
        this.mockMvc.perform(
                put("/clients/1/deposite?money=500&accountId=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(true)));
    }

    @Test
    public void withdrawMoneyTest() throws Exception {
        Client client = new Client("withdraw", "money");
        ObjectMapper mapper = new ObjectMapper();
        given(withdrawService.withdraw( anyLong(), anyInt())).willReturn(100L);
        given(clientService.getById(anyInt())).willReturn(client);

        this.mockMvc.perform(
                put("/clients/5/withdraw?sum=100&accountId=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(100L)));
    }

    @Test
    public void checkBalanceTest() throws Exception {
        Client client = new Client("check", "balance");
        client.addAccount(new Account());
        ObjectMapper mapper = new ObjectMapper();
        given(checkBalanceService.checkBalance(eq(client), anyInt())).willReturn(new CheckBalanceService.Balance(client, 500L));
        given(clientService.getById(anyInt())).willReturn(client);

        this.mockMvc.perform(
                get("/clients/3/balance?accountId=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new CheckBalanceService.Balance(client, 500L))));
    }
}
