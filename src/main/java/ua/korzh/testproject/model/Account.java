package ua.korzh.testproject.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Objects;
import java.util.*;

@Entity
@Table(name = "ACCOUNT", uniqueConstraints = @UniqueConstraint(columnNames = {"BALANCE", "NATURAL_ID", "CLIENT"}))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "BALANCE", nullable = false)
    @Min(0)
    private long balance;
    @Version
    private long version;
    @Column(name = "NATURAL_ID", nullable = false)
    @Min(0)
    private  int naturalId;
    @Column(name = "CLOSED")
    private boolean closed;
    @ManyToOne(optional = false)
    @JoinColumn(name = "CLIENT")
    private Client client;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "account", fetch = FetchType.LAZY)
    private final List<Transaction> transactions = new ArrayList<>();

    public Account() {
    }

    public Account(int naturalId, long balance) {
        this.naturalId = naturalId;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return this.client;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id = " + id +
                ", balance = " + balance +
                ", clients id =" + client.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return naturalId == account.naturalId &&
                client.equals(account.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naturalId, client);
    }
}
