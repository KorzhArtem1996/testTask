package ua.korzh.testTask.accountService;

import ua.korzh.testTask.model.Account;

public interface AccountService {
    public Account create(long balance);

    public boolean addMoney(Account account, long balance);

    public long withDrawMoney(Account account, long sum);
}
