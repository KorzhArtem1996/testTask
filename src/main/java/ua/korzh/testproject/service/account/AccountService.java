package ua.korzh.testproject.service.account;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.model.Client;
import java.util.List;

public interface AccountService {

    Account create(Client client);

    Account deposit(long money, int accountId);

    Account withdraw(long sum, int accountId);

    long checkBalance(int accountId);

    List<Transaction> history(int accountId);

    boolean close(int accountId);
}
