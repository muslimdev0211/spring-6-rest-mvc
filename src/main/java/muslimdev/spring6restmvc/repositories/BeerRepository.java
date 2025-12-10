package muslimdev.spring6restmvc.repositories;

import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Page<Beer> findAllByBeerNameContainingIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerNameContainingIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
}
