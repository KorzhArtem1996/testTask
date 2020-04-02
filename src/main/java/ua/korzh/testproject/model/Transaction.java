package ua.korzh.testproject.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT_HISTORY")
public class Transaction {
    public Transaction() {}
    public Transaction(OperationName operationName, LocalDateTime timestamp) {
        this.operationName = operationName;
        this.timestamp = timestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OPERATION_NAME", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationName operationName;

    @Column(name = "BALANCE_BEFORE", nullable = false)
    private long balance;

    @Column(name = "AMOUNT", nullable = false)
    private long amount;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;


    @ManyToOne(optional = false)
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    public OperationName getOperationName() {
        return operationName;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getTimestamp() { return this.timestamp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return balance == that.balance &&
                amount == that.amount &&
                operationName == that.operationName &&
                timestamp.equals(that.timestamp) &&
                account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationName, balance, amount, timestamp, account);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "operationName=" + operationName +
                ", balance=" + balance +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", account=" + account +
                '}';
    }
}
