package muslimdev.spring6restmvc.Services;

import muslimdev.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerId(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateById(UUID id, BeerDTO beer);

    void deleteById(UUID beerId);

    void patchUpdate(UUID beerId, BeerDTO beer);
}
