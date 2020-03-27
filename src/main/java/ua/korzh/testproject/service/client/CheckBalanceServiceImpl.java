package ua.korzh.testproject.service.client;

import org.springframework.stereotype.Service;
import ua.korzh.testproject.model.Client;

@Service
public class CheckBalanceServiceImpl implements CheckBalanceService {
    @Override
    public CheckBalanceService.Balance checkBalance(Client client, int accountId) {
        if (client != null) {
            long bal = client.getAccount(accountId).getBalance();
            return new CheckBalanceService.Balance(client, bal);
        }
        return null;
    }
}
