package ua.korzh.testproject.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.*;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.OperationName;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.TransactionRepository;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_NOT_EXISTS = "Account with id %d does not exist";
    private static final String POSITIVE_SUM = "Sum of money must be positive";
    private static final String NOT_ENOUGH_MONEY = "You do not have enough money! Balance: %d";

    @Autowired
    private AcountRepository acountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
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

    private void createTransaction(OperationName operationName, Account account, long money) {
        Transaction transaction = new Transaction(OperationName.DEPOSIT, LocalDateTime.now());
        transaction.setBalance(account.getBalance());
        transaction.setAmount(money);
        transaction.setAccount(account);
        switch (operationName) {
            case DEPOSIT: account.setBalance(account.getBalance() + money); break;
            case WITHDRAW: account.setBalance(account.getBalance() - money); break;
        }
        account.addTransaction(transaction);
        transactionRepository.saveAndFlush(transaction);
        acountRepository.saveAndFlush(account);
    }

    @Override
    public void addMoney(Account account, long balance) {
        createTransaction(OperationName.DEPOSIT, account, balance);
    }

    @Override
    public void withDrawMoney(Account account, long sum) {
        if (sum > account.getBalance()) {
            throw new NotEnoughMoneyException(String.format(NOT_ENOUGH_MONEY, account.getBalance()));
        } else {
            createTransaction(OperationName.WITHDRAW, account, sum);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account deposite(long money, int accountId) {
        if (money < 0) throw new NegativeSumException(POSITIVE_SUM);
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));
        addMoney(account, money);
        return account;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account withdraw(long sum, int accountId) {
        if (sum < 0) throw new NegativeSumException(POSITIVE_SUM);
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));
        withDrawMoney(account, sum);
        return account;
    }

    @Override
    public long checkBalance(int accountId) {
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));
        return account.getBalance();
    }

    @Override
    public List<Transaction> history(int accountId) {
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));
        return account.getTransactions();
    }
}
