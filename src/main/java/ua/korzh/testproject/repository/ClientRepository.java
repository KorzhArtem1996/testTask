package ua.korzh.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.korzh.testproject.model.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client getById(int id);
}
