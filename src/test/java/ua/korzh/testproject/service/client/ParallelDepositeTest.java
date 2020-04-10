package ua.korzh.testproject.service.client;

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
    }
}
