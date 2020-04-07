package ua.korzh.testproject.service.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.exception.NegativeSumException;
import ua.korzh.testproject.exception.NotEnoughMoneyException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.repository.TransactionRepository;
import ua.korzh.testproject.service.account.AccountService;
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
    private AccountService accountService;
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
        Client artem = clientService.register("artem", "1234");

        Exception exception = assertThrows(EmailExistsException.class, () -> {
            Client korzh = clientService.register("artem", "5678");
        });
        Client actual = clientService.getById(artem.getId());

        assertEquals(artem, actual);
    }

    @Test
    @Transactional
    void deposit() {
        Client client = clientService.register("deposite", "ssss");
        Exception exception = assertThrows(NegativeSumException.class, () -> {
            clientService.deposit(-55L, client.getAccountsId().get(0));
        });
        Account actualAccount = clientService.deposit( 500L, client.getAccountsId().get(0));

        long actualBalance = actualAccount.getBalance();

        assertEquals(500L, actualBalance);

    }

    @Test
    @Transactional
    void withdraw() {
        Client client = clientService.register("withdraw", "lllll");
        clientService.deposit(100L, client.getAccountsId().get(0));
        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            Account res = clientService.withdraw(120L, client.getAccountsId().get(0));
        });
        Account actualAccount = clientService.withdraw(50L, client.getAccountsId().get(0));

        long actualBalance = actualAccount.getBalance();

        assertEquals(50L, actualBalance);
    }

    @Test
    @Transactional
    void checkBalance() {
        Client client = clientService.register("check", "cccc");
        Account actualAccount = clientService.deposit(500L, client.getAccountsId().get(0));

        long actualBalance = clientService.checkBalance(actualAccount.getId());

        assertEquals(500L, actualBalance);
    }

    @Test
    @Transactional
    void showTransaction() {
        Client client = clientService.register("show", "history");
        clientService.deposit(100L, client.getAccountsId().get(0));
        clientService.deposit(30L, client.getAccountsId().get(0));
        int id = clientService.withdraw(100L, client.getAccountsId().get(0)).getId();
        Account account = acountRepository.getById(id);

        List<Transaction> actualTransactions = account.getTransactions();

        assertEquals(actualTransactions, clientService.showTransaction(account.getId()));
    }
}