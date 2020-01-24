package ua.korzh.testTask.clientService;

import ua.korzh.testTask.model.Client;

public interface CheckBalanceService {
    public Balance checkBalance(Client client);
    public static class Balance {
        private final double balance;
        private final Client client;

        public Balance(Client client, double balance) {
            this.client = client;
            this.balance = balance;
        }
        public double getBalance() {
            return balance;
        }
        public Client getClient() {
            return new Client(client);
        }
        @Override
        public String toString() {
            return "Client " + client.getEmail() + " has balance " + balance;
        }
    }
}
