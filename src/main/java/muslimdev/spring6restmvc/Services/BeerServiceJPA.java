package muslimdev.spring6restmvc.Services;

import lombok.RequiredArgsConstructor;
import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.mappers.BeerMapper;
import muslimdev.spring6restmvc.model.BeerDTO;
import muslimdev.spring6restmvc.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    @Override
    public List<BeerDTO> listBeers() {

        return beerRepository.findAll()
                .stream()
                .map(beerMapper :: beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerId(UUID uuid) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(uuid)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return  beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public void updateById(UUID id, BeerDTO beer) {

    }

    @Override
    public void deleteById(UUID beerId) {

    }

    @Override
    public void patchUpdate(UUID beerId, BeerDTO beer) {

    }
}
