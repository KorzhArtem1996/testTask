package ua.korzh.testTask.clientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import java.util.concurrent.atomic.AtomicBoolean;

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

    @Test
    public void parrllelDepositeTest() throws InterruptedException {
        Client client = singUpService.register("parallel_deposite", "s");

//        for (int i = 0; i < 3; i++) {
//            int finalI = i;
//            new Thread(() -> {
//                Client client1 = clientRepository.getById(client.getId());
//                depositeService.deposite(client1, 5L + finalI);
//            }).start();
//        }
        AtomicBoolean b1 = new AtomicBoolean(false);
        AtomicBoolean b2 = new AtomicBoolean(false);
        Thread t1 = new Thread(() -> {
           Client client1 = clientRepository.getById(client.getId());
           depositeService.deposite(client1, 10L);
        });
        Thread t2 = new Thread(() -> {
            Client client1 = clientRepository.getById(client.getId());
            depositeService.deposite(client1, 20L);
        });
        t1.start();
        t2.start();
        Thread.sleep(3000);

    }
}