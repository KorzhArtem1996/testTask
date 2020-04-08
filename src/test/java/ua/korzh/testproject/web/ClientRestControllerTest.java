package ua.korzh.testproject.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.service.client.ClientService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ClientRestControllerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;

    @Test
    public void getAllClientsTest() throws Exception {
        List<Client> list = Stream
                .of(new Client("client1", "password1"), new Client("client2", "password2"),
                        new Client("client3", "password3"))
                .collect(Collectors.toList());
        given(clientService.getAll()).willReturn(list);

        ResultActions resultActions = this.mockMvc.perform(get("/clients").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(list)));
    }

    @Test
    public void registerClientTest() throws Exception {
        Client client = new Client("email", "password");
        given(clientService.register(anyString(), anyString())).willReturn(client).willThrow(EmailExistsException.class);

        ResultActions resultActions = this.mockMvc.perform(
                post("/clients?email=email&password=password").accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(content().string(MAPPER.writeValueAsString(client)));
        resultActions = this.mockMvc.perform(
                post("/clients?email=email&password=password").accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isConflict());
    }
}