package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountService;
import ua.korzh.testproject.service.client.ClientService;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class CheckBalanceServiceImplTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @Test
    @Transactional
    public void checkBalance() {
        Client client = clientService.register("check", "cccc");
        clientService.deposit(500L, client.getAccountsId().get(0));
        Client client1 = clientRepository.getById(client.getId());
        long balance = accountService.checkBalance(client1.getAccountsId().get(0));
        assertEquals(500L, balance);
    }
}