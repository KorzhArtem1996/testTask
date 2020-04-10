package ua.korzh.testproject.service.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.NegativeSumException;
import ua.korzh.testproject.exception.NotEnoughMoneyException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.service.client.ClientService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceImplTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AcountRepository acountRepository;

    @Test
    @Transactional
    void deposit() {
        long money = 500L;
        Client client = clientService.register("deposite", "ssss");
        long initialBalance = client.getAccount(client.getAccountsId().get(0)).getBalance();
        Account actualAccount = accountService.deposit( money, client.getAccountsId().get(0));

        long actualBalance = actualAccount.getBalance();

        assertEquals(initialBalance + money, actualBalance);
        Exception exception = assertThrows(NegativeSumException.class, () -> {
            accountService.deposit(-55L, client.getAccountsId().get(0));
        });
    }

    @Test
    @Transactional
    void withdraw() {
        Client client = clientService.register("withdraw", "lllll");
        long sum = 50L;
        accountService.deposit(100L, client.getAccountsId().get(0));
        long initialBalance = client.getAccount(client.getAccountsId().get(0)).getBalance();
        Account actualAccount = accountService.withdraw(sum, client.getAccountsId().get(0));

        long actualBalance = actualAccount.getBalance();

        assertEquals(initialBalance + sum, actualBalance);
        Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
            Account res = accountService.withdraw(120L, client.getAccountsId().get(0));
        });
    }

    @Test
    @Transactional
    void checkBalance() {
        Client client = clientService.register("check", "cccc");
        long money = 500L;
        long initialBalance = client.getAccount(client.getAccountsId().get(0)).getBalance();
        Account actualAccount = accountService.deposit(money, client.getAccountsId().get(0));

        long actualBalance = accountService.checkBalance(actualAccount.getId());

        assertEquals(initialBalance + money, actualBalance);
    }

    @Test
    @Transactional
    void showTransaction() {
        Client client = clientService.register("show", "history");
        long money = 100L;
        accountService.deposit(money, client.getAccountsId().get(0));
        accountService.deposit(30L, client.getAccountsId().get(0));
        int id = accountService.withdraw(money, client.getAccountsId().get(0)).getId();
        Account account = acountRepository.getById(id);

        List<Transaction> actualTransactions = account.getTransactions();

        assertEquals(3, actualTransactions.size());
        assertEquals(actualTransactions, accountService.history(account.getId()));
    }
}
