package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    @Autowired
    private AccountService accountService;

    @Override
    public double withdraw(Client client, double sum) {
        if (client != null) {
            Account account = client.getAccount();
            return accountService.withDrawMoney(account, sum);
        }
        return -1d;
    }
}
