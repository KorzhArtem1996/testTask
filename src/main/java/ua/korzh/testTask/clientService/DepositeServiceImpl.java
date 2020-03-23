package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;

import javax.persistence.LockModeType;

@Service
public class DepositeServiceImpl implements DepositeService {
    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public boolean deposite(Client client, long money) {
        if (client != null && money >= 0){
            Account account = client.getAccount();

            accountService.addMoney(account, money);
            return true;
        }
        return false;
    }
}
