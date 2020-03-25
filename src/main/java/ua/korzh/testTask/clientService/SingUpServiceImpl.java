package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.exception.EmailExistsException;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import java.util.List;
@Service
public class SingUpServiceImpl implements SingUpService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @Override
    public Client register(String email, String password) {
        List<Client> clients = clientService.getAll();
        for (Client cl : clients) {
            if (cl.getEmail().equals(email)) throw new EmailExistsException("E-mail \'" + email + "\' already exists");
        }
        Client client = new Client(email, password);
        clientRepository.save(client);
        Account account = accountService.create(0, client);
        client.setAccount(account);
        return client;
    }

}


