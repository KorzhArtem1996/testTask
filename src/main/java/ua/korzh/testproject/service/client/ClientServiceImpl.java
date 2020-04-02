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
            LOGGER.error("Caught EmailExistsException", e);
            throw new RuntimeException(e);
        }
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(client);
        client.addAccount(account);
        LOGGER.info("register(" + email + ", " + password + ") succeeded");
        return client;
    }

    @Override
    public Account deposit(long money, int accountId) {
        Account account;
        try {
            account = accountService.deposite(money, accountId);
        } catch (NegativeSumException e) {
            LOGGER.error("Caught NegativeSumException", e);
            throw new RuntimeException(e);
        } catch (NegativeAccountIdException e) {
            LOGGER.error("Caught NegativeAccountIdException", e);
            throw new RuntimeException(e);
        } catch (AccountNotExistException e) {
            LOGGER.error("Caught AccountNotExistException", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("deposit(" + money + ", " + accountId + ") succeeded");
        return account;
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        Account account;
        try {
            account = accountService.withdraw(sum, accountId);
        } catch (NegativeSumException e) {
            LOGGER.error("Caught NegativeSumException", e);
            throw new RuntimeException(e);
        } catch (NegativeAccountIdException e) {
            LOGGER.error("Caught NegativeAccountIdException", e);
            throw new RuntimeException(e);
        } catch (AccountNotExistException e) {
            LOGGER.error("Caught AccountNotExistException", e);
            throw new RuntimeException(e);
        } catch (NotEnoughMoneyException e) {
            LOGGER.error("Caught NotEnoughtMoneyException");
            throw new RuntimeException(e);
        }
        LOGGER.info("withdraw(" + sum + ", " + accountId + ") succeeded");
        return account;
    }

    @Override
    public long checkBalance(int accountId) {
        long res = accountService.checkBalance(accountId);
        LOGGER.info("checkBalance(" + accountId + ") succeeded");
        return res;
    }

    @Override
    public List<Transaction> showTransaction(int accountId) {
        return accountService.history(accountId);
    }
}
