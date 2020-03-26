package ua.korzh.testproject.clientservice;

import ua.korzh.testproject.model.Client;

public interface SingUpService {
    public Client register(String email, String password);
}
