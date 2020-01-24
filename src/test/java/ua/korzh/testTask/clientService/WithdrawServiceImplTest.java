package ua.korzh.testTask.clientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WithdrawServiceImplTest {
    @Autowired
    private SingUpService singUpService;
    @Autowired
    private DepositeService depositeService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WithdrawService withdrawService;

    @Test
    public void withDrawTest() {
        Client client = singUpService.register("withdraw", "l");
        depositeService.deposite(client, 100);
        Client client1 = clientRepository.getById(client.getId());
        double res = withdrawService.withdraw(client1, 120);
        assertEquals(100d, res);
        assertEquals(0d, client1.getAccount().getBalance());
    }


}