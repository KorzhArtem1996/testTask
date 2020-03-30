package ua.korzh.testproject.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"BALANCE", "NATURAL_ID", "CLIENT"}))
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

    public Account() {}

    public Account(long balance, int naturalId) {
        this.balance = balance;
        this.naturalId = naturalId;
    }

    public int getId() {return id;}

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "CLIENT")
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return this.client;
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
        return balance == account.balance &&
                naturalId == account.naturalId &&
                client.equals(account.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, naturalId, client);
    }
}
