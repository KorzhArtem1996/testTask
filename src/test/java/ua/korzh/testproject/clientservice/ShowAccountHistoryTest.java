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

        assertEquals("[AccountHistory{operationName='Deposit: 100', balanceBefore=0, balanceAfter=100, account=Account{id = 7, balance = 30, clients id =7}}, AccountHistory{operationName='Deposit: 30', balanceBefore=100, balanceAfter=130, account=Account{id = 7, balance = 30, clients id =7}}, AccountHistory{operationName='Withdraw: 100', balanceBefore=130, balanceAfter=30, account=Account{id = 7, balance = 30, clients id =7}}]", clientService.showAccountHistory(client.getAccountsId().get(0)).toString());
    }

}
