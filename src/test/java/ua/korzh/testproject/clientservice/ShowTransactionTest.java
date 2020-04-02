package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.service.client.ClientService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ShowTransactionTest {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AcountRepository acountRepository;

    @Test
    @Transactional
    public void showHistory() {
        Client client = clientService.register("show", "history");
        clientService.deposit(100L, client.getAccountsId().get(0));
        clientService.deposit(30L, client.getAccountsId().get(0));
        clientService.withdraw(100L, client.getAccountsId().get(0));

        assertEquals(3, clientService.showTransaction(client.getAccountsId().get(0)).size());
    }
}
