package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.client.DepositeService;
import ua.korzh.testproject.service.client.SingUpService;

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
        Account account = client.getAccount(client.getAccountsId().get(0));
        assertNotNull(account);
        assertEquals(0L, account.getBalance());
        depositeService.deposite( 500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        assertEquals(500L, client1.getAccount(client1.getAccountsId().get(0)).getBalance());
    }
}