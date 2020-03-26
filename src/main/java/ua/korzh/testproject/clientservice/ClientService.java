package ua.korzh.testproject.clientservice;

import ua.korzh.testproject.model.Client;

import java.util.List;

public interface ClientService {
    public List<Client> getAll();
    public Client getById(int id);
}
