package muslimdev.spring6restmvc.bootstrap;

import muslimdev.spring6restmvc.Services.BeerCSVService;
import muslimdev.spring6restmvc.Services.BeerCSVServiceImpl;
import muslimdev.spring6restmvc.Services.BeerOrderService;
import muslimdev.spring6restmvc.repositories.BeerOrderRepository;
import muslimdev.spring6restmvc.repositories.BeerRepository;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(BeerCSVServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    ClientRepository clientRepository;

    BootstrapData bootstrapData;

    @Autowired
    BeerCSVService csvService;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, clientRepository, csvService, beerOrderRepository);
    }

    @Test
    void Testrun() throws Exception {
        bootstrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(clientRepository.count()).isEqualTo(3);
    }
}