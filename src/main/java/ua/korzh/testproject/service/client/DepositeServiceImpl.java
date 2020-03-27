package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.korzh.testproject.service.account.AccountService;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.repository.AcountRepository;
import ua.korzh.testproject.repository.ClientRepository;

@Service
public class DepositeServiceImpl implements DepositeService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AcountRepository acountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deposite(long money, int accountId) {
        if (money >= 0 && accountId >= 0){
            Account account = acountRepository.getById(accountId);
            accountService.addMoney(account, money);
            return true;
        }
        return false;
    }
}
