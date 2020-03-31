package ua.korzh.testproject.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "EMAIL", unique = true, nullable = false)
    @Size(min = 4)
    private String email;
    @Column(name = "PASSWORD", nullable = false)
    @Size(min = 4)
    private String password;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public Client() {}

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Client(Client client) {
        this.email = client.email;
        this.password = client.password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accounts=" + accounts +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public Account getAccount(int id) {
        for (Account account : accounts) {
            if (account.getId() == id) return account;
        }
        return null;
    }
    public List<Integer> getAccountsId() {
        List<Integer> res = new ArrayList<>();
        for (Account account : accounts) {
            res.add(account.getId());
        }
        return res;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return email.equals(client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
