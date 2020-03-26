package ua.korzh.testproject.clientservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.accountservice.AccountService;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.AcountRepository;

import javax.persistence.LockModeType;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AcountRepository acountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public long withdraw(Client client, long sum, int accountId) {
        if (client != null) {
            Account account = acountRepository.getById(accountId);
            return accountService.withDrawMoney(account, sum);
        }
        return -1L;
    }
}
