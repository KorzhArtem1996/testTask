package ua.korzh.testproject.service.client;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.exception.NegativeAccountIdException;
import ua.korzh.testproject.exception.NegativeSumException;
import ua.korzh.testproject.exception.NotEnoughMoneyException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientServiceImplTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Order(1)
    public void getAll() {
        List<Client> all = clientService.getAll();
        assertNotNull(all);
        assertEquals(Collections.emptyList(), all);
    }

    @Test
    @Order(2)
    @Transactional
    public void getById() {
        Client client = clientService.register("getById", "12345");
        Client client2 = clientService.getById(client.getId());
        assertEquals("Client{id=4, email='getById', password='12345', accounts=[Account{id = 4, balance = 0, clients id =4}]}", client.toString());
        assertEquals(client, client2);
        assertEquals(client.getAccount(client.getAccountsId().get(0)), client2.getAccount(client2.getAccountsId().get(0)));
        assertNull(client.getAccount(55));
    }

    @Test
    @Transactional
    void register() {
        Client artem = clientService.register("artem", "1234");
        Exception exception = assertThrows(EmailExistsException.class, () -> {
            Client korzh = clientService.register("artem", "5678");
        });
        List<Client> list = clientRepository.findAll();
        assertEquals(artem, list.get(1));
    }

    @Test
    @Transactional
    void deposit() {
        Client client = clientService.register("deposite", "ssss");
        Account account = client.getAccount(client.getAccountsId().get(0));
        assertEquals("Account{id = 6, balance = 0, clients id =6}", account.toString());
        assertEquals(0L, account.getBalance());
        clientService.deposit( 500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        assertEquals(500L, client1.getAccount(client1.getAccountsId().get(0)).getBalance());
        Exception exception = assertThrows(NegativeSumException.class, () -> {
           clientService.deposit(-55L, client1.getAccountsId().get(0));
        });
        Exception exception2 = assertThrows(NegativeAccountIdException.class, () -> {
            clientService.deposit(50L, -5);
        });
    }

    @Test
    @Transactional
    void withdraw() {
        Client client = clientService.register("withdraw", "lllll");
        clientService.deposit(100L, client.getAccountsId().get(0));
        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            Account res = clientService.withdraw(120L, client.getAccountsId().get(0));
        });
    }

    @Test
    @Transactional
    void checkBalance() {
        Client client = clientService.register("check", "cccc");
        clientService.deposit(500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        long balance = clientService.checkBalance(client1.getAccountsId().get(0));
        assertEquals(500L, balance);
    }

    @Test
    @Transactional
    void showTransaction() {
        Client client = clientService.register("show", "history");
        clientService.deposit(100L, client.getAccountsId().get(0));
        clientService.deposit(30L, client.getAccountsId().get(0));
        clientService.withdraw(100L, client.getAccountsId().get(0));
        assertEquals(3, clientService.showTransaction(client.getAccountsId().get(0)).size());

    }
}