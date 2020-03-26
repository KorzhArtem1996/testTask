package ua.korzh.testproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.korzh.testproject.exception.EmailExistsException;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.clientservice.*;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private SingUpService singUpService;
    @Autowired
    private DepositeService depositeService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private  CheckBalanceService checkBalanceService;

    @GetMapping("/clients")
    public List<Client> getAll() {
        return clientService.getAll();
    }

    @PostMapping("/register")
    public Optional<Client> register(@RequestParam String email, @RequestParam String password) {
        Client client;
        Optional<Client> opt;
        try {
             client = singUpService.register(email, password);
             opt = Optional.ofNullable(client);
        } catch (EmailExistsException e) {
            opt = Optional.empty();
        }
        return opt;
    }

    @PutMapping("/clients/{id}/deposite")
    public Boolean deposite(@PathVariable int id, @RequestParam Long money, @RequestParam int accountId) {
        Client client = clientService.getById(id);
        return depositeService.deposite(client, money, accountId);
    }

    @PutMapping("/clients/{id}/withdraw")
    public Long withdraw(@PathVariable int id, @RequestParam Long sum, int accountId) {
        Client client = clientService.getById(id);
        return withdrawService.withdraw(client, sum, accountId);
    }

    @GetMapping("/clients/{id}/balance")
    public CheckBalanceService.Balance checkBalance(@PathVariable int id, int accountId) {
        Client client = clientService.getById(id);
        return checkBalanceService.checkBalance(client, accountId);
    }
}
