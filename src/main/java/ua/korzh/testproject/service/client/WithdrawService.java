package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Client;

public interface WithdrawService {
    public long withdraw(long sum, int accountId);
}
