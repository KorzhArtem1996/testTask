package ua.korzh.testTask.clientService;

import ua.korzh.testTask.model.Client;

public interface WithdrawService {
    public long withdraw(Client client, long sum);
}
