package ua.korzh.testproject.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.*;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AcountRepository acountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Value("${account.default.balance}")
    private long defaultBalance;
    @Override
    public Account create(Client client) {
        Account account = new Account(client.getAccountsId().size(), defaultBalance);
        account.setClient(client);
        acountRepository.saveAndFlush(account);
        return account;
    }


    @Override
    public void addMoney(Account account, long balance) {
            account.setBalance(account.getBalance() + balance);
            acountRepository.saveAndFlush(account);
    }

    @Override
    public void withDrawMoney(Account account, long sum) {
        if (sum <= account.getBalance()) {
            account.setBalance(account.getBalance() - sum);
            acountRepository.saveAndFlush(account);

        } else {
            throw new NotEnoughMoney("You do not have enough money! Balance: " + account.getBalance());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account deposite(long money, int accountId) {
        if (money < 0) throw new NegativeSumException("Sum of money must be positive");
        if (accountId < 0) throw new NegativeAccountIdException("Accounts id must be positive");
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException("Account with id <" + accountId + "> does not exist");
        addMoney(account, money);
        return account;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account withdraw(long sum, int accountId) {
        if (sum < 0) throw new NegativeSumException("Sum of money must be positive");
        if (accountId < 0) throw new NegativeAccountIdException("Accounts id must be positive");
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException("Account with id <" + accountId + "> does not exist");
        withDrawMoney(account, sum);
        return account;
    }



    @Override
    public long checkBalance(int accountId) {
        if (accountId < 0) throw new NegativeAccountIdException("Accounts id must be positive");
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException("Account with id <" + accountId + "> does not exist");
        long bal = account.getBalance();
            return bal;
    }

}
