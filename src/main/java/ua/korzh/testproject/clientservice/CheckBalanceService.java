package ua.korzh.testproject.clientservice;

import ua.korzh.testproject.model.Client;

public interface CheckBalanceService {
    public Balance checkBalance(Client client, int accountId);
    public static class Balance {
        private final long balance;
        private final Client client;

        public Balance(Client client, long balance) {
            this.client = client;
            this.balance = balance;
        }
        public long getBalance() {
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
