package ua.korzh.testproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.service.client.*;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @PostMapping("/register")
    public Optional<Client> register(@RequestParam String email, @RequestParam String password) {
        Client client;
        Optional<Client> opt;
        try {
             client = clientService.register(email, password);
             opt = Optional.ofNullable(client);
        } catch (EmailExistsException e) {
            opt = Optional.empty();
        }
        return opt;
    }

    @PutMapping("/clients/{id}/deposite")
    public Account deposite(@PathVariable int id, @RequestParam Long money, @RequestParam int accountId) {
        Client client = clientService.getById(id);
        return clientService.deposite(money, accountId);
    }

    @PutMapping("/clients/{id}/withdraw")
    public Account withdraw(@PathVariable int id, @RequestParam Long sum, int accountId) {
        Client client = clientService.getById(id);
        return clientService.withdraw(sum, accountId);
    }

    @GetMapping("/clients/{id}/balance")
    public CheckBalanceService.Balance checkBalance(@PathVariable int id, int accountId) {
        Client client = clientService.getById(id);
        return clientService.checkBalance(accountId);
    }
}
