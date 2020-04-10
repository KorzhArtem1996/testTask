package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Client;
import java.util.List;

public interface ClientService {

    List<Client> getAll();

    Client getById(int id);

    Client register(String email, String password);
}
