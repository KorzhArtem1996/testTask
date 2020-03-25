package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.exception.EmailExistsException;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import java.util.HashSet;
import java.util.List;
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
    public Client register(String email, String password) {
        if (emails.contains(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        emails.add(email);
        Client client = new Client(email, password);
        clientRepository.save(client);
        Account account = accountService.create(0, client);
        client.setAccount(account);
        return client;
    }
}


