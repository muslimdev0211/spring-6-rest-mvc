package muslimdev.spring6restmvc.repositories;

import muslimdev.spring6restmvc.entities.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    void testSaveClient(){

        Client client = clientRepository.save(Client.builder()
                .clientName("UHUH")
                .build());

        assertThat(client).isNotNull();
        assertThat(client.getId()).isNotNull();

    }

}