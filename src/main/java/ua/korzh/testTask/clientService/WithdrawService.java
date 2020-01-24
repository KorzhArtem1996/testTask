package ua.korzh.testTask.clientService;

import ua.korzh.testTask.model.Client;

public interface WithdrawService {
    public double withdraw(Client client, double sum);
}
