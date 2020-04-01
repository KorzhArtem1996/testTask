package ua.korzh.testproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.korzh.testproject.model.AccountHistory;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer> {
}
