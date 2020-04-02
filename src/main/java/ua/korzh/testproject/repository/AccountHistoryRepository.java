package ua.korzh.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.korzh.testproject.model.Transaction;

public interface AccountHistoryRepository extends JpaRepository<Transaction, Integer> {
}
