package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.service.client.ClientService;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ShowAccountHistoryTest {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AcountRepository acountRepository;


    @Test
    @Transactional
    public void showHistory() {
        Client client = clientService.register("show", "history");
        clientService.deposite(100L, client.getAccountsId().get(0));
        clientService.deposite(30L, client.getAccountsId().get(0));
        clientService.withdraw(100L, client.getAccountsId().get(0));

        assertEquals(3, clientService.showAccountHistory(client.getAccountsId().get(0)).size());
    }

}
