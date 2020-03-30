package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.account.AccountService;

import java.util.List;
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

    @Override
    public Client register(String email, String password) {
        return accountService.register(email, password);
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
