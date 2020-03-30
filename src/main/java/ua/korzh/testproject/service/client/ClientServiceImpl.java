package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {
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
        if (emails.contains(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(client);
        client.addAccount(account);
        return client;
    }

    @Override
    public Account deposite(long money, int accountId) {
        return accountService.deposite(money, accountId);
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        return accountService.withdraw(sum, accountId);
    }

    @Override
    public AccountService.Balance checkBalance(int accountId) {
        return accountService.checkBalance(accountId);
    }
}
