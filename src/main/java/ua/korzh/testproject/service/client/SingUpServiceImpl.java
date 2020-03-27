package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.service.account.AccountService;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class SingUpServiceImpl implements SingUpService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    private static Set<String> emails = new HashSet<>();
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public Client register(String email, String password) {
        if (emails.contains(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.saveAndFlush(client);
        Account account = accountService.create(0, client);
        client.addAccount(account);
        return client;
    }
}


