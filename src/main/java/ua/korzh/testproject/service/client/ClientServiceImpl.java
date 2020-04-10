package ua.korzh.testproject.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.*;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountService;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientServiceImpl implements ClientService, InitializingBean {

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
    private static final String EMAIL_EXISTS_EXCEPTION = "E-mail '%s' already exists";

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(int id) {
        return clientRepository.getById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public Client register(String email, String password) {
        LOGGER.info(REGISTER_START_MESSAGE, email);
        boolean isAdded = EMAILS.add(email);
        if (!isAdded) {throw new EmailExistsException(String.format(EMAIL_EXISTS_EXCEPTION, email));}
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(client);
        client.addAccount(account);
        LOGGER.info(REGISTER_SUCCESS_MESSAGE, email);
        return client;
    }

    @Override
    public Account deposit(long money, int accountId) {
        LOGGER.info(DEPOSIT_START_MESSAGE, money, accountId);
        Account account = accountService.deposit(money, accountId);
        LOGGER.info(DEPOSIT_SUCCESS_MESSAGE, money, accountId);
        return account;
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        LOGGER.info(WITHDRAW_START_MESSAGE, sum, accountId);
        Account account = accountService.withdraw(sum, accountId);
        LOGGER.info(WITHDRAW_SUCCESS_MESSAGE, sum, accountId);
        return account;
    }

    @Override
    public long checkBalance(int accountId) {
        LOGGER.info(CHECK_BALANCE_START_MESSAGE, accountId);
        long res = accountService.checkBalance(accountId);
        LOGGER.info(CHECK_BALANCE_SUCCESS_MESSAGE, accountId);
        return res;
    }

    @Override
    public List<Transaction> showTransaction(int accountId) {
        LOGGER.info(SHOW_TRANSACTION_START_MESSAGE, accountId);
        List<Transaction> res = accountService.history(accountId);
        LOGGER.info(SHOW_TRANSACTION_SUCCESS_MESSAGE, accountId);
        return res;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> clients = clientRepository.getAllUserEmails();
        EMAILS.addAll(clients);
    }
}
