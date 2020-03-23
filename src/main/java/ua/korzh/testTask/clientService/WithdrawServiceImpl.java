package ua.korzh.testTask.clientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testTask.accountService.AccountService;
import ua.korzh.testTask.model.Account;
import ua.korzh.testTask.model.Client;

import javax.persistence.LockModeType;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    @Autowired
    private AccountService accountService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public long withdraw(Client client, long sum) {
        if (client != null) {
            Account account = client.getAccount();
            return accountService.withDrawMoney(account, sum);
        }
        return -1L;
    }
}
