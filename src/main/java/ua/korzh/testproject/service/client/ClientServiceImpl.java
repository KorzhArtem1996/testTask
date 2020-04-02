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
        }
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(client);
        client.addAccount(account);
        LOGGER.debug(String.format("register(%s, %s) succeeded", email, password));
        return client;
    }

    @Override
    public Account deposit(long money, int accountId) {
        Account account = null;
        try {
            account = accountService.deposite(money, accountId);
        } catch (NegativeSumException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NegativeAccountIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (AccountNotExistException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.debug(String.format("deposit(%d, %d) succeeded", money, accountId));
        return account;
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        Account account = null;
        try {
            account = accountService.withdraw(sum, accountId);
        } catch (NegativeSumException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NegativeAccountIdException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (AccountNotExistException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NotEnoughMoneyException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.debug(String.format("withdraw(%d, %d) succeeded", sum, accountId));
        return account;
    }

    @Override
    public long checkBalance(int accountId) {
        long res = accountService.checkBalance(accountId);
        LOGGER.debug(String.format("checkBalance(%d) succeeded", accountId));
        return res;
    }

    @Override
    public List<Transaction> showTransaction(int accountId) {
        List<Transaction> res = accountService.history(accountId);
        LOGGER.debug(String.format("showTransaction(%d) succeeded", accountId));
        return res;
    }
}
