package ua.korzh.testproject.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.korzh.testproject.model.Account;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;

import java.util.List;
@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SingUpService singUpService;
    @Autowired
    private DepositeService depositeService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private CheckBalanceService checkBalanceService;

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(int id) {
        return clientRepository.getById(id);
    }

    @Override
    public Client register(String email, String password) {
        return singUpService.register(email, password);
    }

    @Override
    public Account deposite(long money, int accountId) {
        return depositeService.deposite(money, accountId);
    }

    @Override
    public Account withdraw(long sum, int accountId) {
        return withdrawService.withdraw(sum, accountId);
    }

    @Override
    public CheckBalanceService.Balance checkBalance(int accountId) {
        return checkBalanceService.checkBalance(accountId);
    }
}
