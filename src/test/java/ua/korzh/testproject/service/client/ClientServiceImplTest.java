package ua.korzh.testproject.service.client;

import org.junit.jupiter.api.BeforeEach;
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

    @Test
    @Transactional
    void deposit() {
        long money = 500L;
        Client client = clientService.register("deposite", "ssss");
        long initialBalance = client.getAccount(client.getAccountsId().get(0)).getBalance();
        Account actualAccount = clientService.deposit( money, client.getAccountsId().get(0));

        long actualBalance = actualAccount.getBalance();

        assertEquals(initialBalance + money, actualBalance);
        Exception exception = assertThrows(NegativeSumException.class, () -> {
            clientService.deposit(-55L, client.getAccountsId().get(0));
        });
    }

    @Test
    @Transactional
    void withdraw() {
        Client client = clientService.register("withdraw", "lllll");
        long sum = 50L;
        clientService.deposit(100L, client.getAccountsId().get(0));
        long initialBalance = client.getAccount(client.getAccountsId().get(0)).getBalance();
        Account actualAccount = clientService.withdraw(sum, client.getAccountsId().get(0));

        long actualBalance = actualAccount.getBalance();

        assertEquals(initialBalance + sum, actualBalance);
        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            Account res = clientService.withdraw(120L, client.getAccountsId().get(0));
        });
    }

    @Test
    @Transactional
    void checkBalance() {
        Client client = clientService.register("check", "cccc");
        long money = 500L;
        long initialBalance = client.getAccount(client.getAccountsId().get(0)).getBalance();
        Account actualAccount = clientService.deposit(money, client.getAccountsId().get(0));

        long actualBalance = clientService.checkBalance(actualAccount.getId());

        assertEquals(initialBalance + money, actualBalance);
    }

    @Test
    @Transactional
    void showTransaction() {
        Client client = clientService.register("show", "history");
        long money = 100L;
        clientService.deposit(money, client.getAccountsId().get(0));
        clientService.deposit(30L, client.getAccountsId().get(0));
        int id = clientService.withdraw(money, client.getAccountsId().get(0)).getId();
        Account account = acountRepository.getById(id);

        List<Transaction> actualTransactions = account.getTransactions();

        assertEquals(3, actualTransactions.size());
        assertEquals(actualTransactions, clientService.showTransaction(account.getId()));
    }
}