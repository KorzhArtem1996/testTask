package ua.korzh.testproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Transaction;
import ua.korzh.testproject.service.account.AccountService;
import java.util.List;

@RestController
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @PutMapping("/clients/{clientId}/accounts/{accountId}/deposit")
    public Account deposit(@PathVariable int clientId, @RequestParam Long money, @PathVariable int accountId) {
        return accountService.deposit(money, accountId);
    }

    @PutMapping("/clients/{clientId}/accounts/{accountId}/withdraw")
    public Account withdraw(@PathVariable int clientId, @RequestParam Long sum, @PathVariable int accountId) {
        return accountService.withdraw(sum, accountId);
    }

    @GetMapping("/clients/{clientId}/accounts/{accountId}/balance")
    public long checkBalance(@PathVariable int clientId, @PathVariable int accountId) {
        return accountService.checkBalance(accountId);
    }

    @GetMapping("/clients/{clientId}/accounts/{accountId}/history")
    public List<Transaction> showHistroy(@PathVariable int clientId, @PathVariable int accountId) {
        return accountService.history(accountId);
    }
}
