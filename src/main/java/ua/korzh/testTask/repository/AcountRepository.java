package ua.korzh.testTask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ua.korzh.testTask.model.Account;

import javax.persistence.LockModeType;

@Repository
public interface AcountRepository extends JpaRepository<Account, Integer> {
}
