package ua.korzh.testTask.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.clientService.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    public void getAllUserEmailsTest() throws Exception {
        List<Client> list = Stream
                .of(new Client("client1", "password1"), new Client("client2", "password2"), new Client("client3", "password3"))
                .collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        given(clientService.getAll()).willReturn(list);
        this.mockMvc.perform(
                get("/get_all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(list)));
    }

    @Test
    public void registerClientTest() throws Exception{
        Client client = new Client("email", "password");
        ObjectMapper mapper = new ObjectMapper();

        given(singUpService.register(anyString(), anyString())).willReturn(client);
        this.mockMvc.perform(
                post("/register?email=email&password=password").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(client)));
    }

    @Test
    public void depositeMoneyTest() throws Exception {
        Client client = new Client("deposite", "money");
        ObjectMapper mapper = new ObjectMapper();
        given(depositeService.deposite(eq(client), anyDouble())).willReturn(false, true);

        this.mockMvc.perform(
                post("/deposite?money=500").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(false)));
        this.mockMvc.perform(
                post("/deposite?money=500").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(true)));
    }

    @Test
    public void withdrawMoneyTest() throws Exception {
        Client client = new Client("withdraw", "money");
        ObjectMapper mapper = new ObjectMapper();
        given(withdrawService.withdraw(eq(client), anyDouble())).willReturn(100d);

        this.mockMvc.perform(
                post("/withdraw?sum=50").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(100d)));
    }

    @Test
    public void checkBalanceTest() throws Exception {
        Client client = new Client("check", "balance");
        client.setAccount(new Account());
        ObjectMapper mapper = new ObjectMapper();
        given(checkBalanceService.checkBalance(client)).willReturn(new CheckBalanceService.Balance(client, 500d));

        this.mockMvc.perform(
                post("/check_balance").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(new CheckBalanceService.Balance(client, 500d))));
    }
}
