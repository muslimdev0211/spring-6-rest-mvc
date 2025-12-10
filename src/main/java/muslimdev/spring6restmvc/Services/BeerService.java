package muslimdev.spring6restmvc.Services;

import muslimdev.spring6restmvc.model.BeerDTO;
import muslimdev.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;


import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
    Optional<BeerDTO> getBeerId(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateById(UUID id, BeerDTO beer);

    Boolean deleteById(UUID beerId);

    Optional<BeerDTO> patchUpdate(UUID beerId, BeerDTO beer);
}
