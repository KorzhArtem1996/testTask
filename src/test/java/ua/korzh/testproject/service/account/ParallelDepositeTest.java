package ua.korzh.testproject.service.account;

import static  org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountServiceImpl;
import ua.korzh.testproject.service.client.ClientService;

import javax.persistence.OptimisticLockException;

@SpringBootTest
public class ParallelDepositeTest {

    @Autowired
    private ClientRepository clientRepository;
    @SpyBean
    private AccountServiceImpl accountService;
    @Autowired
    private ClientService clientService;

    @Test
    @Transactional
    public void parallelDepositTest() {
        long money = 100L;
        doAnswer(invocationOnMock -> {
            accountService.deposit(150L, any());
            return invocationOnMock.callRealMethod();
        }).when(accountService).createTransaction(any(), any(), eq(money));

        Exception exception = assertThrows(OptimisticLockException.class, () ->{
            accountService.deposit(money, 1);
        });
    }
}
