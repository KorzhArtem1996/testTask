package ua.korzh.testTask.clientService;

import ua.korzh.testTask.model.Client;

public interface SingUpService {
    public Client register(String email, String password);
}
