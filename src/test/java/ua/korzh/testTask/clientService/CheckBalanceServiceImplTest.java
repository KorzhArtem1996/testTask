package ua.korzh.testTask.clientService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CheckBalanceServiceImplTest {
    @Autowired
    private SingUpService singUpService;
    @Autowired
    private DepositeService depositeService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CheckBalanceService checkBalanceService;

    @Test
    public void checkBalance() {
        Client client = singUpService.register("check", "c");
        depositeService.deposite(client, 500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        CheckBalanceService.Balance balance = checkBalanceService.checkBalance(client1, client1.getAccountsId().get(0));
        assertEquals(500L, balance.getBalance());
    }
}