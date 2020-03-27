package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;

@Service
public class CheckBalanceServiceImpl implements CheckBalanceService {
    @Autowired
    private AcountRepository acountRepository;
    @Override
    public CheckBalanceService.Balance checkBalance(int accountId) {
        if (accountId >= 0) {
            Account account = acountRepository.getById(accountId);
            long bal = account.getBalance();
            return new CheckBalanceService.Balance(account.getClient(), bal);
        }
        return null;
    }
}
