package ua.korzh.testproject.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.AccountNotExistException;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.exception.NegativeAccountIdException;
import ua.korzh.testproject.exception.NegativeSumException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountService;

import java.util.HashSet;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AcountRepository acountRepository;
    @Autowired
    private ClientRepository clientRepository;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account deposite(long money, int accountId) {
        if (money < 0) throw new NegativeSumException("Sum of money must be positive");
        if (accountId < 0) throw new NegativeAccountIdException("Accounts id must be positive");
        Account account = acountRepository.getById(accountId);
        boolean res = addMoney(account, money);
        if (res == false) throw new AccountNotExistException("Account with id <" + accountId + "> does not exist");
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

    private static Set<String> emails = new HashSet<>();
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public Client register(String email, String password) {
        if (emails.contains(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = create(0, client);
        client.addAccount(account);
        return client;
    }

}
