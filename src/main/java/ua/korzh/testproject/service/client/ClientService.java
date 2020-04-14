package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Client;
import java.util.List;

/**
 * This interface represents service for {@link Client} entity.
 *
 * @author Korzh Artem
 * @version 1.0
 */
public interface ClientService {

    /**
     * Returns list of all clients.
     * @return List of all clients or empty list if any client was not found
     */
    List<Client> getAll();

    /**
     * Returns client by id.
     * @param id the identifier of client
     * @return client with required identifier or null if client is not found
     */
    Client getById(int id);

    /**
     * Registers client.
     * @param email the e-mail of client
     * @param password the password of client
     * @throws ua.korzh.testproject.exception.EmailExistsException if client with this e-mail already exists
     * @return client that was registered
     */
    Client register(String email, String password);

}
