package ua.korzh.testproject.service.account;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;

public interface AccountService {
    public Account create(long balance, Client client);

    public boolean addMoney(Account account, long balance);

    public long withDrawMoney(Account account, long sum);

    public Account deposite(long money, int accountId);

    public Account withdraw(long sum, int accountId);

    Client register(String email, String password);

    public AccountService.Balance checkBalance(int accountId);
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
