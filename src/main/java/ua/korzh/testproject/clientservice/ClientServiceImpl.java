package ua.korzh.testproject.clientservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;

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
