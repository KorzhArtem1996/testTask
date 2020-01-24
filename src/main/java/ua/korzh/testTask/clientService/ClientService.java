package ua.korzh.testTask.clientService;

import ua.korzh.testTask.model.Client;

import java.util.List;

public interface ClientService {
    public List<Client> getAll();
    public Client getById(int id);
}
