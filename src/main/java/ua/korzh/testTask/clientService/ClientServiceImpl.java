package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import java.util.List;
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(int id) {
        return clientRepository.getById(id);
    }
}
