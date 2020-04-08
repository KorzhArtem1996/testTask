package ua.korzh.testproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.service.client.ClientService;
import java.util.List;

@RestController
public class ClientRestController {

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
}
