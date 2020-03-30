package ua.korzh.testproject.service.account;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;

public interface AccountService {
    public Account create(long balance, Client client);

    public boolean addMoney(Account account, long balance);

    public long withDrawMoney(Account account, long sum);

    public Account deposite(long money, int accountId);

    public Account withdraw(long sum, int accountId);

    Client register(String email, String password);
}
