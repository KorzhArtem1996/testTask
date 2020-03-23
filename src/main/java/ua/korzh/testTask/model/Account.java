package ua.korzh.testTask.model;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "BALANCE")
    private long balance;
    @Version
    private long version;

    public Account() {}

    public Account(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @OneToOne(optional = false, mappedBy = "account")
    private Client client;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", client=" + client +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                client.equals(account.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client);
    }
}
