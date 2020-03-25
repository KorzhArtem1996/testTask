package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;
import ua.korzh.testTask.repository.ClientRepository;

import javax.persistence.LockModeType;

@Service
public class DepositeServiceImpl implements DepositeService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deposite(Client client, long money, int accountId) {
        if (client != null && money >= 0){
            Account account = client.getAccount(accountId);
            accountService.addMoney(account, money);
            return true;
        }
        return false;
    }
}
