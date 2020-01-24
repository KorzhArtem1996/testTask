package ua.korzh.testTask.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.korzh.testTask.exception.EmailExistsException;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.clientService.*;

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

    @GetMapping("get_all")
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

    @PostMapping("/deposite")
    public Boolean deposite(@RequestBody Client client, @RequestParam Double money) {
        return depositeService.deposite(client, money);
    }

    @PostMapping("/withdraw")
    public Double withdraw(@RequestBody Client client, @RequestParam Double sum) {
        return withdrawService.withdraw(client, sum);
    }

    @PostMapping("/check_balance")
    public CheckBalanceService.Balance checkBalance(@RequestBody Client client) {
        return checkBalanceService.checkBalance(client);
    }
}
