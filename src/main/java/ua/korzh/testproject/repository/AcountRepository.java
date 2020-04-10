package ua.korzh.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.korzh.testproject.model.Account;

public interface AcountRepository extends JpaRepository<Account, Integer> {
    Account getById(int id);
}
