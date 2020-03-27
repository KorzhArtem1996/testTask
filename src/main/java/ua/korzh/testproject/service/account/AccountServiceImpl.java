package ua.korzh.testproject.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.service.account.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AcountRepository acountRepository;
    @Override
    public Account create(long balance, Client client) {
        Account account = new Account(balance, client.getAccountsId().size());
        account.setClient(client);
        acountRepository.saveAndFlush(account);
        return account;
    }

    @Override
    public boolean addMoney(Account account, long balance) {
        if (account != null) {
            account.setBalance(account.getBalance() + balance);
            acountRepository.saveAndFlush(account);
            return true;
        }
        return false;
    }

    @Override
    public long withDrawMoney(Account account, long sum) {
        if (account == null) return -1;
        long res = sum;
        if (sum <= account.getBalance()) {
            account.setBalance(account.getBalance() - sum);

        } else {
            res = account.getBalance();
            account.setBalance(0L);
        }
        acountRepository.saveAndFlush(account);
        return res;
    }

}
