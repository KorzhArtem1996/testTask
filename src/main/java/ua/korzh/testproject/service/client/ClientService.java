package ua.korzh.testproject.service.client;

import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Transaction;
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

    /**
     * Allows to deposit money to the client's account.
     * @param money sum of money to be deposited to the account
     * @param accountId the identifier of client's account
     * @throws ua.korzh.testproject.exception.NegativeSumException if sum of money is negative
     * @throws ua.korzh.testproject.exception.AccountNotExistException if account with this id does not exist
     * @return account with refreshed balance
     */
    Account deposit(long money, int accountId);

    /**
     * Allows to withdraw sum of money from client's account.
     * @param sum sum of money to be withdrawn
     * @param accountId the identifier of client's account
     * @throws ua.korzh.testproject.exception.NegativeSumException if sum of money is negative
     * @throws ua.korzh.testproject.exception.AccountNotExistException if account with this id does not exist
     * @throws ua.korzh.testproject.exception.NotEnoughMoneyException is account with this id doesn't have enough money
     * @return account with refreshed balance
     */
    Account withdraw(long sum, int accountId);

    /**
     * Allows to check the balance of client's account.
     * @param accountId the identifier of client's account
     * @return balance of client's account
     */
    long checkBalance(int accountId);

    /**
     * Allows to watch the list of operations with client's account.
     * @param accountId the identifier of client's account
     * @return List of transactions with client's account
     */
    List<Transaction> showTransaction(int accountId);
}
