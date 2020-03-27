package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.client.DepositeService;
import ua.korzh.testproject.service.client.SingUpService;
import ua.korzh.testproject.service.client.WithdrawService;

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
        depositeService.deposite(100L, client.getAccountsId().get(0));
        Account res = withdrawService.withdraw(120L, client.getAccountsId().get(0));
        assertEquals(0L, res.getBalance());
        Client client1 = clientRepository.getById(client.getId());
        assertEquals(0L, client1.getAccount(client1.getAccountsId().get(0)).getBalance());
    }


}