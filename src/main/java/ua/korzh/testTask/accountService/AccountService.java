package ua.korzh.testTask.accountService;

import ua.korzh.testTask.model.Account;

public interface AccountService {
    public Account create(double balance);

    public boolean addMoney(Account account, double balance);

    public double withDrawMoney(Account account, double sum);
}
