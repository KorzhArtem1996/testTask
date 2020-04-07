package ua.korzh.testproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Transaction;
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

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public Client register(@RequestParam String email, @RequestParam String password) {
        return clientService.register(email, password);
    }

    @PutMapping("/clients/{clientId}/accounts/{accountId}/deposit")
    public Account deposit(@PathVariable int clientId, @RequestParam Long money, @PathVariable int accountId) {
        return clientService.deposit(money, accountId);
    }

    @PutMapping("/clients/{clientId}/accounts/{accountId}/withdraw")
    public Account withdraw(@PathVariable int clientId, @RequestParam Long sum, @PathVariable int accountId) {
        return clientService.withdraw(sum, accountId);
    }

    @GetMapping("/clients/{clientId}/accounts/{accountId}/balance")
    public long checkBalance(@PathVariable int clientId, @PathVariable int accountId) {
        return clientService.checkBalance(accountId);
    }

    @GetMapping("/clients/{clientId}/accounts/{accountId}/history")
    public List<Transaction> showHistroy(@PathVariable int clientId, @PathVariable int accountId) {
        return clientService.showTransaction(accountId);
    }
}
