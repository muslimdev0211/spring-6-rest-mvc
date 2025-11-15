package muslimdev.spring6restmvc.bootstrap;

import muslimdev.spring6restmvc.repositories.BeerRepository;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    ClientRepository clientRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, clientRepository);
    }

    @Test
    void Testrun() throws Exception {
        bootstrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(clientRepository.count()).isEqualTo(3);
    }
}