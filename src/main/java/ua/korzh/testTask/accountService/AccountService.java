package ua.korzh.testTask.accountService;

import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;

public interface AccountService {
    public Account create(long balance, Client client);

    public boolean addMoney(Account account, long balance);

    public long withDrawMoney(Account account, long sum);
}
