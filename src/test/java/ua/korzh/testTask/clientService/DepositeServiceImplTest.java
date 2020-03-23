package ua.korzh.testTask.clientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DepositeServiceImplTest {
    @Autowired
    private SingUpService singUpService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DepositeService depositeService;

    @Test
    public void depositeTest() {
        Client client = singUpService.register("deposite", "s");
        Client client1 = clientRepository.getById(client.getId());
        Account account = client1.getAccount();
        assertNotNull(account);
        assertEquals(0L, account.getBalance());
        depositeService.deposite(client1, 500L);
        assertEquals(500L, client1.getAccount().getBalance());

    }
}