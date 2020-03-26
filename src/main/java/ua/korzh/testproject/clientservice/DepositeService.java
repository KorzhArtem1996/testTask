package ua.korzh.testproject.clientservice;

import ua.korzh.testproject.model.Client;

public interface DepositeService {
    public boolean deposite(Client client, long money, int accountId);
}
