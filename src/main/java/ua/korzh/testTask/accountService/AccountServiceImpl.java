package ua.korzh.testTask.accountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.repository.AcountRepository;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AcountRepository acountRepository;
    @Override
    public Account create(double balance) {
        Account account = new Account(balance);
        acountRepository.save(account);
        return account;
    }

    @Override
    @Transactional
    public boolean addMoney(Account account, double balance) {
        if (account != null) {
            account.setBalance(account.getBalance() + balance);
            acountRepository.saveAndFlush(account);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public double withDrawMoney(Account account, double sum) {
        if (account == null) return -1;
        double res = sum;
        if (sum <= account.getBalance()) {
            account.setBalance(account.getBalance() - sum);

        } else {
            res = account.getBalance();
            account.setBalance(0d);
        }
        acountRepository.saveAndFlush(account);
        return res;
    }

}
