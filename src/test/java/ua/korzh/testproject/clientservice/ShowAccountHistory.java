package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.service.client.ClientService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ShowAccountHistory {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AcountRepository acountRepository;


    @Test
    public void showHistory() {
        Client client = clientService.register("show", "history");
        clientService.deposite(100L, client.getAccountsId().get(0));
        clientService.deposite(30L, client.getAccountsId().get(0));
        clientService.withdraw(100L, client.getAccountsId().get(0));
        assertEquals("[Deposit: 100, Deposit: 30, Withdraw: 100]", clientService.showAccountHistory(client.getAccountsId().get(0)).toString());
    }

}
