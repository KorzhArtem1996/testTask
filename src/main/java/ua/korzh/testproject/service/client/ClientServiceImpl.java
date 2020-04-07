package ua.korzh.testproject.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

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

    private static final Set<String> emails = new HashSet<>();
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public Client register(String email, String password) {
        final String startMessage = "register(email, password) started, email={}";
        final String successMessage = "register(email, password) succeeded, email={}";
        LOGGER.info(startMessage, email);
        if (emails.contains(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(client);
        client.addAccount(account);
        LOGGER.info(successMessage, email);
        return client;
    }

    @Override
    public Account deposit(long money, int accountId) {

        final String startMessage = "deposit(money, accountId) started, money={}, accountId={}";
        final String successMessage = "deposit(money, accountId) succeeded, money={}, accountId={}";
        LOGGER.info(startMessage, money, accountId);
        Account account = accountService.deposite(money, accountId);
        LOGGER.info(successMessage, money, accountId);
        return account;
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        Account account;
        final String startMessage = "withdraw(sum, accountId) started, sum={}, accountId={}";
        final String successMessage = "withdraw(sum, accountId) succeeded, sum={}, accountId={}";
        LOGGER.info(startMessage, sum, accountId);
        account = accountService.withdraw(sum, accountId);
        LOGGER.info(successMessage, sum, accountId);
        return account;
    }

    @Override
    public long checkBalance(int accountId) {
        final String startMessage = "checkBalance(accountId) started, accountId={}";
        final String successMessage = "checkBalance(accountId) succeeded, accountId={}";
        LOGGER.info(startMessage, accountId);
        long res = accountService.checkBalance(accountId);
        LOGGER.info(successMessage, accountId);
        return res;
    }

    @Override
    public List<Transaction> showTransaction(int accountId) {
        final String startMessage = "showTransaction(accountId) started, accountId={}";
        final String successMessage = "showTransaction(accountId) succeeded, accountId={}";
        LOGGER.info(startMessage, accountId);
        List<Transaction> res = accountService.history(accountId);
        LOGGER.info(successMessage, accountId);
        return res;
    }
}
