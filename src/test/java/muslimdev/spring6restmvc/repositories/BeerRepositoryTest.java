package muslimdev.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

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