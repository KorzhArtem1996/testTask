package ua.korzh.testproject.service.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.repository.TransactionRepository;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceImplTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AcountRepository acountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void clearDB() {
        clientRepository.deleteAll();
        acountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    public void getAll() {
        Client client1 = clientService.register("email1", "password1");
        Client client2 = clientService.register("email2", "password2");
        List<Client> clients = Arrays.asList(client1, client2);

        List<Client> actual = clientService.getAll();

        assertEquals(clients, actual);
    }

    @Test
    @Transactional
    public void getById() {
        Client expected = clientService.register("getById", "12345");

        Client actual = clientService.getById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void register() {
        String email = "artem";
        Client artem = clientService.register(email, "1234");

        Client actual = clientService.getById(artem.getId());

        assertEquals(artem, actual);
        Exception exception = assertThrows(EmailExistsException.class, () -> {
            Client korzh = clientService.register(email, "5678");
        });
    }
}