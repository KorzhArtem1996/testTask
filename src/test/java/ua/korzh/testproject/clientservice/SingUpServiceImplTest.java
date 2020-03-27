package ua.korzh.testproject.clientservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.korzh.testproject.model.Client;
import ua.korzh.testproject.repository.ClientRepository;
import ua.korzh.testproject.service.client.SingUpService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SingUpServiceImplTest {
    @Autowired
    private SingUpService singUpService;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void registerTest() {
        Client artem = singUpService.register("artem", "1");
        Client korzh = singUpService.register("korzh", "2");
        assertNotNull(korzh);
        assertNotEquals(artem, korzh);
        List<Client> list = clientRepository.findAll();
        assertEquals(korzh, list.get(1));
    }
}