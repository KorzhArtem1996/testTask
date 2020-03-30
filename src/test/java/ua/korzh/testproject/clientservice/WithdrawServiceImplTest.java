package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.exception.NotEnoughMoney;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.client.ClientService;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WithdrawServiceImplTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;

    @Test
    public void withDrawTest() {
        Client client = clientService.register("withdraw", "l");
        clientService.deposite(100L, client.getAccountsId().get(0));
        Exception exception = assertThrows(NotEnoughMoney.class, () -> {
            Account res = clientService.withdraw(120L, client.getAccountsId().get(0));
        });
    }


}