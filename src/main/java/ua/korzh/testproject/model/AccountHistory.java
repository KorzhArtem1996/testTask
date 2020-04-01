package ua.korzh.testproject.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT_HISTORY")
public class AccountHistory {
    public AccountHistory() {}
    public AccountHistory(String operationName) {
        this.operationName = operationName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OPERATION_NAME", nullable = false)
    private String operationName;

    @Column(name = "BALANCE_BEFORE", nullable = false)
    private long balanceBefore;

    @Column(name = "BALANCE_AFTER", nullable = false)
    private long balanceAfter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    public String getOperationName() {
        return operationName;
    }

    public long getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(long balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public long getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "AccountHistory{" +
                "operationName='" + operationName + '\'' +
                ", balanceBefore=" + balanceBefore +
                ", balanceAfter=" + balanceAfter +
                ", account=" + account +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountHistory that = (AccountHistory) o;
        return balanceBefore == that.balanceBefore &&
                balanceAfter == that.balanceAfter &&
                operationName.equals(that.operationName) &&
                account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationName, balanceBefore, balanceAfter, account);
    }
}
