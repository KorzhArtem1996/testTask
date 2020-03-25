package ua.korzh.testTask.model;

import javax.persistence.*;
import java.util.*;

@Entity(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    public void setAccount(Account account) {
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
        return id == client.id &&
                email.equals(client.email) && password.equals(client.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
