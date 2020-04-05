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

    private static Set<String> emails = new HashSet<>();
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public Client register(String email, String password) {
        try {
            if (emails.contains(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        } catch (EmailExistsException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(client);
        client.addAccount(account);
        LOGGER.info("register(email, password) succeeded, email={}", email);
        return client;
    }

    @Override
    public Account deposit(long money, int accountId) {
        Account account;
        final String errorMessage = "deposit(money, accountId) error, money={}, accountId={}";
        final String successMessage = "deposit(money, accountId) succeeded, money={}, accountId={}";
        try {
            account = accountService.deposite(money, accountId);
            LOGGER.info(successMessage, money, accountId);
            return account;
        } catch (NegativeSumException | NegativeAccountIdException | AccountNotExistException e) {
            LOGGER.error(errorMessage, money, accountId, e);
            throw e;
        }
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        Account account;
        final String errorMessage = "withdraw(sum, accountId) error, sum={}, accountId={}";
        final String successMessage = "withdraw(sum, accountId) succeeded, sum={}, accountId={}";
        try {
            account = accountService.withdraw(sum, accountId);
            LOGGER.info(successMessage, sum, accountId);
            return account;
        } catch (NegativeSumException | NegativeAccountIdException | AccountNotExistException | NotEnoughMoneyException e) {
            LOGGER.error(errorMessage, sum, accountId, e);
            throw e;
        }
    }

    @Override
    public long checkBalance(int accountId) {
        long res = accountService.checkBalance(accountId);
        LOGGER.info("checkBalance(accountId) succeeded, accountId={}", accountId);
        return res;
    }

    @Override
    public List<Transaction> showTransaction(int accountId) {
        List<Transaction> res = accountService.history(accountId);
        LOGGER.info("showTransaction(accountId) succeeded, accountId={}", accountId);
        return res;
    }
}
