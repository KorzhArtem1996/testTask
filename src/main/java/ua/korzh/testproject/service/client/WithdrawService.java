package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;

public interface WithdrawService {
    public Account withdraw(long sum, int accountId);
}
