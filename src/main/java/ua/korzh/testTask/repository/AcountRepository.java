package ua.korzh.testTask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.korzh.testTask.model.Account;

@Repository
public interface AcountRepository extends JpaRepository<Account, Integer> {
}
