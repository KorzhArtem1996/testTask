package ua.korzh.testproject.service.account;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;

public interface AccountService {
    public Account create(Client client);

    public void addMoney(Account account, long balance);

    public void withDrawMoney(Account account, long sum);

    public Account deposite(long money, int accountId);

    public Account withdraw(long sum, int accountId);

    public long checkBalance(int accountId);

}
