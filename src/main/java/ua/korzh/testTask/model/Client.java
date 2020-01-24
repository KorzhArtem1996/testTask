package ua.korzh.testTask.model;

import javax.persistence.*;
import java.util.Objects;
@Entity(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ACCOUNT")
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
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
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public Account getAccount() {
        return account;
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
                email.equals(client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
