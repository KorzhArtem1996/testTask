package ua.korzh.testTask.clientService;

import ua.korzh.testTask.model.Client;

public interface DepositeService {
    public boolean deposite(Client client, long money);
}
