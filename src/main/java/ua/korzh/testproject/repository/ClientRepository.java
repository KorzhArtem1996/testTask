package ua.korzh.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.korzh.testproject.model.Client;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client getById(int id);

    @Query("SELECT c.email FROM Client c")
    List<String> getAllUserEmails();
}
