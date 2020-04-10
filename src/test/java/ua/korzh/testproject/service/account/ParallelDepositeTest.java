package ua.korzh.testproject.service.account;

import static  org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.repository.TransactionRepository;
import ua.korzh.testproject.service.client.ClientService;

@SpringBootTest
public class ParallelDepositeTest {

    @SpyBean
    private AccountServiceImpl accountService;
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
    @Transactional
    public void parallelDepositTest() {
        Client client = clientService.register("parallel", "deposit");
        long money = 100L;
        doAnswer(invocationOnMock -> {
            accountService.deposit(150L, client.getAccountsId().get(0));
            return invocationOnMock.callRealMethod();
        }).when(accountService).createTransaction(any(), any(), eq(money));

        Exception exception = assertThrows(ObjectOptimisticLockingFailureException.class, () ->{
            accountService.deposit(money, client.getAccountsId().get(0));
        });
    }
}
