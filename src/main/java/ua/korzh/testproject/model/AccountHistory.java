package ua.korzh.testproject.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT_HISTORY")
public class AccountHistory {
    public AccountHistory() {}
    public AccountHistory(String operationName, LocalDateTime localDateTime) {
        this.operationName = operationName;
        this.localDateTime = localDateTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OPERATION_NAME", nullable = false)
    private String operationName;

    @Column(name = "DATE_TIME", nullable = false)
    private LocalDateTime localDateTime;

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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
