package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;

public interface DepositeService {
    public Account deposite(long money, int accountId);
}
