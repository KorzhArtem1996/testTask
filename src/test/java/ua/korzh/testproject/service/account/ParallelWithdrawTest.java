package ua.korzh.testproject.service.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.repository.TransactionRepository;
import ua.korzh.testproject.service.client.ClientService;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParallelWithdrawTest {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AcountRepository acountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @SpyBean
    protected AccountServiceImpl accountService;

    @BeforeEach
    public void clearDB() {
        clientRepository.deleteAll();
        acountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    public void parallelWithdrawTest() {
        Client client = clientService.register("parallelWithdraw", "withdraw");
        accountService.deposit(500L, client.getAccountsId().get(0));
        long money = 100L;
        doAnswer(invocationOnMock -> {
            accountService.withdraw(150L, client.getAccountsId().get(0));
            return invocationOnMock.callRealMethod();
        }).when(accountService).createTransaction(any(), any(), eq(money));

        Exception exception = assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            accountService.withdraw(money, client.getAccountsId().get(0));
        });
    }
}
