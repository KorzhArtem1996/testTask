package ua.korzh.testproject.clientservice;

import static  org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import ua.korzh.testproject.service.account.AccountServiceImpl;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.client.ClientService;

@SpringBootTest
public class ParallelDepositeTest {

    @Autowired
    private ClientRepository clientRepository;
    @SpyBean
    private AccountServiceImpl accountService;
    @Autowired
    private ClientService clientService;
    @Test
    public void parallelDepositeTest() {
        Client client = clientService.register("parallel", "deposite");
        Client client1 = clientRepository.getById(client.getId());
        doAnswer(invocationOnMock -> {
            clientService.deposite(19L, client1.getAccountsId().get(0));
            Object res = invocationOnMock.callRealMethod();
            return res;
        }).when(accountService).addMoney(any(), eq(5L));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> clientService.deposite(5L, client1.getAccountsId().get(0)));
    }
}
