package ua.korzh.testTask.clientService;

import static  org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import ua.korzh.testTask.accountService.AccountServiceImpl;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

@SpringBootTest
public class ParallelDepositeTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SingUpService singUpService;
    @SpyBean
    private AccountServiceImpl accountService;
    @Autowired
    private DepositeService depositeService;

    @Test
    public void parallelDepositeTest() {
        Client client = singUpService.register("parallel", "deposite");
        Client client1 = clientRepository.getById(client.getId());
        doAnswer(invocationOnMock -> {
            depositeService.deposite(client1, 19L);
            Object res = invocationOnMock.callRealMethod();
            return res;
        }).when(accountService).addMoney(any(), eq(5L));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> depositeService.deposite(client1, 5L));
    }
}
