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

    @PutMapping("/clients/{clientId}/accounts/{accountId}/deposit")
    public Account deposit(@PathVariable int clientId, @RequestParam Long money, @PathVariable int accountId) {
        Client client = clientService.getById(clientId);
        return clientService.deposit(money, accountId);
    }

    @PutMapping("/clients/{clientId}/accounts/{accountId}/withdraw")
    public Account withdraw(@PathVariable int clientId, @RequestParam Long sum, @PathVariable int accountId) {
        Client client = clientService.getById(clientId);
        return clientService.withdraw(sum, accountId);
    }

    @GetMapping("/clients/{clientId}/accounts/{accountId}/balance")
    public long checkBalance(@PathVariable int clientId, @PathVariable int accountId) {
        Client client = clientService.getById(clientId);
        return clientService.checkBalance(accountId);
    }

}
