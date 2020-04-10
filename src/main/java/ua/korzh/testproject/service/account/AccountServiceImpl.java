package ua.korzh.testproject.service.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ua.korzh.testproject.service.client.ClientServiceImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_NOT_EXISTS = "Account with id %d does not exist";
    private static final String POSITIVE_SUM = "Sum of money must be positive";
    private static final String NOT_ENOUGH_MONEY = "You do not have enough money! Balance: %d";
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);
    private static final Set<String> EMAILS = ConcurrentHashMap.newKeySet();
    private static final String REGISTER_START_MESSAGE = "register(email, password) started, email={}";
    private static final String REGISTER_SUCCESS_MESSAGE = "register(email, password) succeeded, email={}";
    private static final String DEPOSIT_START_MESSAGE = "deposit(money, accountId) started, money={}, accountId={}";
    private static final String DEPOSIT_SUCCESS_MESSAGE = "deposit(money, accountId) succeeded, money={}, accountId={}";
    private static final String WITHDRAW_START_MESSAGE = "withdraw(sum, accountId) started, sum={}, accountId={}";
    private static final String WITHDRAW_SUCCESS_MESSAGE = "withdraw(sum, accountId) succeeded, sum={}, accountId={}";
    private static final String CHECK_BALANCE_START_MESSAGE = "checkBalance(accountId) started, accountId={}";;
    private static final String CHECK_BALANCE_SUCCESS_MESSAGE = "checkBalance(accountId) succeeded, accountId={}";
    private static final String SHOW_TRANSACTION_START_MESSAGE = "showTransaction(accountId) started, accountId={}";
    private static final String SHOW_TRANSACTION_SUCCESS_MESSAGE = "showTransaction(accountId) succeeded, accountId={}";


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

    void createTransaction(OperationName operationName, Account account, long money) {
        Transaction transaction = new Transaction(operationName, LocalDateTime.now());
        transaction.setBalance(account.getBalance());
        transaction.setAmount(money);
        transaction.setAccount(account);
        switch (operationName) {
            case DEPOSIT:
                account.setBalance(account.getBalance() + money);
                break;
            case WITHDRAW:
                account.setBalance(account.getBalance() - money);
                break;
            default:
                break;
        }
        account.addTransaction(transaction);
        transactionRepository.saveAndFlush(transaction);
        acountRepository.saveAndFlush(account);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account deposit(long money, int accountId) {
        LOGGER.info(DEPOSIT_START_MESSAGE, money, accountId);
        if (money < 0) throw new NegativeSumException(POSITIVE_SUM);
        Account account = acountRepository.getById(accountId);
        if (account == null) {throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));}
        createTransaction(OperationName.DEPOSIT, account, money);
        LOGGER.info(DEPOSIT_SUCCESS_MESSAGE, money, accountId);
        return account;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account withdraw(long sum, int accountId) {
        LOGGER.info(WITHDRAW_START_MESSAGE, sum, accountId);
        if (sum < 0) throw new NegativeSumException(POSITIVE_SUM);
        Account account = acountRepository.getById(accountId);
        if (account == null) {throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));}
        if (sum > account.getBalance()) {
            throw new NotEnoughMoneyException(String.format(NOT_ENOUGH_MONEY, account.getBalance()));
        }
        createTransaction(OperationName.WITHDRAW, account, sum);
        LOGGER.info(WITHDRAW_SUCCESS_MESSAGE, sum, accountId);
        return account;
    }

    @Override
    public long checkBalance(int accountId) {
        LOGGER.info(CHECK_BALANCE_START_MESSAGE, accountId);
        Account account = acountRepository.getById(accountId);
        if (account == null) {throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));}
        long balance = account.getBalance();
        LOGGER.info(CHECK_BALANCE_SUCCESS_MESSAGE, accountId);
        return balance;
    }

    @Override
    public List<Transaction> history(int accountId) {
        LOGGER.info(SHOW_TRANSACTION_START_MESSAGE, accountId);
        Account account = acountRepository.getById(accountId);
        if (account == null) {throw new AccountNotExistException(String.format(ACCOUNT_NOT_EXISTS, accountId));}
        List<Transaction> transactions = account.getTransactions();
        LOGGER.info(SHOW_TRANSACTION_SUCCESS_MESSAGE, accountId);
        return transactions;
    }
}
