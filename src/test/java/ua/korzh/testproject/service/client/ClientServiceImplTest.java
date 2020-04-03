package ua.korzh.testproject.service.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.NotEnoughMoneyException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountService;

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
    private AccountService accountService;


    @Test
    @Transactional
    void register() {
        Client artem = clientService.register("artem", "1234");
        Client korzh = clientService.register("korzh", "5678");
        assertNotNull(korzh);
        assertNotEquals(artem, korzh);
        List<Client> list = clientRepository.findAll();
        assertEquals(artem, list.get(1));
    }

    @Test
    @Transactional
    void deposit() {
        Client client = clientService.register("deposite", "ssss");
        Account account = client.getAccount(client.getAccountsId().get(0));
        assertNotNull(account);
        assertEquals(0L, account.getBalance());
        clientService.deposit( 500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        assertEquals(500L, client1.getAccount(client1.getAccountsId().get(0)).getBalance());
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
        long balance = accountService.checkBalance(client1.getAccountsId().get(0));
        assertEquals(500L, balance);
    }

    @Test
    @Transactional
    void showTransaction() {
        Client client = clientService.register("show", "history");
        clientService.deposit(100L, client.getAccountsId().get(0));
        clientService.deposit(30L, client.getAccountsId().get(0));
        clientService.withdraw(100L, client.getAccountsId().get(0));

        Assertions.assertEquals(3, clientService.showTransaction(client.getAccountsId().get(0)).size());

    }
}