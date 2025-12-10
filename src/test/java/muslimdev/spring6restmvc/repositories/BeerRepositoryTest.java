package muslimdev.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import muslimdev.spring6restmvc.Services.BeerCSVServiceImpl;
import muslimdev.spring6restmvc.bootstrap.BootstrapData;
import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.flyway.enabled=false"
//})
@DataJpaTest
@Import({BootstrapData.class, BeerCSVServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testGetBeerListByName() {
        Page<Beer> beerList = beerRepository.findAllByBeerNameContainingIgnoreCase("IPA", null);
//        System.out.println(beerList.getContent().size());
        assertThat(beerList.getContent().size()).isEqualTo(336);

    }

    @Test
    void testSaveBeerNameToLong() {

            assertThrows(ConstraintViolationException.class, () -> {
                Beer savedBeer = beerRepository.save(Beer.builder()
                        .beerName("My Beer 0123456789012345678901234567890123456789012345678901234567890123456789")
                        .beerStyle(BeerStyle.LAGER)
                        .upc("242312313")
                        .price(new BigDecimal("33.22"))
                        .build());
                beerRepository.flush();
            });
    }


@Rollback
@Transactional
@Test
void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder()
            .beerName("My Beer")
            .beerStyle(BeerStyle.LAGER)
            .upc("242312313")
            .price(new BigDecimal("33.22"))
            .build());
    beerRepository.flush();

    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();
}
}