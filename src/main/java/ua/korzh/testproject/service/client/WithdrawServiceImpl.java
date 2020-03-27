package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.exception.AccountNotExistException;
import ua.korzh.testproject.exception.NegativeAccountIdException;
import ua.korzh.testproject.exception.NegativeSumException;
import ua.korzh.testproject.service.account.AccountService;
import ua.korzh.testproject.model.Account;
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
    public Account withdraw(long sum, int accountId) {
        if (sum < 0) throw new NegativeSumException("Sum of money must be positive");
        if (accountId < 0) throw new NegativeAccountIdException("Accounts id must be positive");
        Account account = acountRepository.getById(accountId);
        if (account == null) throw new AccountNotExistException("Account with id <" + accountId + "> does not exist");
        accountService.withDrawMoney(account, sum);
        return account;
    }
}
