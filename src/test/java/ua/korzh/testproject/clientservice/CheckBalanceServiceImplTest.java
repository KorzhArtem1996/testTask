package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.client.CheckBalanceService;
import ua.korzh.testproject.service.client.ClientService;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CheckBalanceServiceImplTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CheckBalanceService checkBalanceService;
    @Autowired
    private ClientService clientService;

    @Test
    public void checkBalance() {
        Client client = clientService.register("check", "c");
        clientService.deposite(500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        CheckBalanceService.Balance balance = checkBalanceService.checkBalance(client1.getAccountsId().get(0));
        assertEquals(500L, balance.getBalance());
    }
}