package ua.korzh.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.korzh.testproject.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client getById(int id);
}
