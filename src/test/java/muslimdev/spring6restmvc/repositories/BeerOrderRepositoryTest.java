package muslimdev.spring6restmvc.repositories;

import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.entities.BeerOrder;
import muslimdev.spring6restmvc.entities.BeerOrderShipment;
import muslimdev.spring6restmvc.entities.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    BeerRepository beerRepository;

    Client testClient;
    Beer testBeer;

    @BeforeEach
     void setUp() {
        testClient = clientRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBeerOrders(){
        BeerOrder beerOrder = BeerOrder.builder()
                .clientRef("Test Order")
                .client(testClient)
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("12345")
                        .build())
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

        System.out.println(savedBeerOrder.getClientRef());
    }
}