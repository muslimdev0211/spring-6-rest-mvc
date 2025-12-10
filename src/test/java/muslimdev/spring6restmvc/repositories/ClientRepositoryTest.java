package muslimdev.spring6restmvc.repositories;

import muslimdev.spring6restmvc.entities.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Rollback
    @Transactional
    @Test
    void testSaveClient(){

        Client client = clientRepository.save(Client.builder()
                .clientName("UHUH")
                .build());

        assertThat(client).isNotNull();
        assertThat(client.getId()).isNotNull();

    }

}