package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.service.account.AccountService;

import java.util.List;

public interface ClientService {
    List<Client> getAll();
    Client getById(int id);
    Client register(String email, String password);
    Account deposite(long money, int accountId);
    Account withdraw(long sum, int accountId);
    long checkBalance(int accountId);
    List<String> showAccountHistory(int accountId);
}
