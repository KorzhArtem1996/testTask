package ua.korzh.testproject.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.repository.TransactionRepository;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionTest {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AcountRepository acountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void clearDB() {
        clientRepository.deleteAll();
        acountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void testEquals() {
        LocalDateTime local = LocalDateTime.now();
        Client client = new Client();
        Account account = new Account(1, 55L);
        account.setClient(client);
        Transaction transaction = new Transaction(OperationName.DEPOSIT, local);
        transaction.setAccount(account);
        Transaction transaction2 = new Transaction(OperationName.DEPOSIT, local);
        transaction2.setAccount(account);

        assertEquals(transaction, transaction2);
    }

    @Test
    void testHashCode() {
        LocalDateTime local = LocalDateTime.now();
        Client client = new Client();
        Account account = new Account(1, 55L);
        account.setClient(client);
        Transaction transaction = new Transaction(OperationName.DEPOSIT, local);
        transaction.setAccount(account);
        Transaction transaction2 = new Transaction(OperationName.WITHDRAW, local);
        transaction2.setAccount(account);

        assertNotEquals(transaction.hashCode(), transaction2.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime local = LocalDateTime.now();
        Client client = new Client();
        Account account = new Account(1, 55L);
        account.setClient(client);
        Transaction transaction = new Transaction(OperationName.DEPOSIT, local);
        transaction.setAccount(account);
        System.out.println(local.toString());

        assertEquals("Transaction{operationName=DEPOSIT, balance=0, amount=0, timestamp="
                        + local.toString()
                        + ", account=Account{id = 0, balance = 55, clients id =0}}", transaction.toString());
    }
}