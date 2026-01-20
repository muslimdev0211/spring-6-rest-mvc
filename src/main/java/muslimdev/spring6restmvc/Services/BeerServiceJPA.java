package muslimdev.spring6restmvc.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.events.BeerCreatedEvent;
import muslimdev.spring6restmvc.events.BeerDeletedEvent;
import muslimdev.spring6restmvc.events.BeerPatchedEvent;
import muslimdev.spring6restmvc.events.BeerUpdatedEvent;
import muslimdev.spring6restmvc.mappers.BeerMapper;
import muslimdev.spring6restmvc.model.BeerDTO;
import muslimdev.spring6restmvc.model.BeerStyle;
import muslimdev.spring6restmvc.repositories.BeerRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle,
                                   Boolean showInventory, Integer pageNumber, Integer pageSize) {
        log.info("List Beers - in service");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeerByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);

        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeerByStyleAndByName(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));


        return beerPage.map(beerMapper::beerToBeerDto);

    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {

        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }

        }

        return PageRequest.of(queryPageNumber, queryPageSize);

    }

    public Page<Beer> listBeerByStyleAndByName(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameContainingIgnoreCaseAndBeerStyle(beerName, beerStyle, null);
    }

    public Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, null);

    }

    public Page<Beer> listBeerByName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameContainingIgnoreCase(beerName, null);
    }

    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public Optional<BeerDTO> getBeerId(UUID id) {
        log.info("Get Beer by iD - in service");

        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        if (cacheManager.getCache("beerListCache") != null){
            cacheManager.getCache("beerListCache").clear();
        }

        val saveBeer = beerRepository.save(beerMapper.beerDtoToBeer(beer));


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        applicationEventPublisher.publishEvent(new BeerCreatedEvent(saveBeer, auth));

        return beerMapper.beerToBeerDto(saveBeer);
    }

    private void clearCache(UUID beerId) {
        if (cacheManager.getCache("beerCache") != null) {
            cacheManager.getCache("beerCache").evict(beerId);
        }
        if (cacheManager.getCache("beerListCache") != null) {
            cacheManager.getCache("beerListCache").clear();
        }
    }

    @Override
    public Optional<BeerDTO> updateById(UUID id, BeerDTO beer) {
        clearCache(id);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            foundBeer.setVersion(beer.getVersion());

            val savedBeer = beerRepository.save(foundBeer);
            val auth = SecurityContextHolder.getContext().getAuthentication();
            applicationEventPublisher.publishEvent(new BeerUpdatedEvent(savedBeer, auth));

            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    //    @Caching(evict = {
//            @CacheEvict(cacheNames = "beerCache", key = "#beerId"),
//            @CacheEvict(cacheNames = "beerListCache")
//    })
    @Override
    public Boolean deleteById(UUID beerId) {
        clearCache(beerId);

        if (beerRepository.existsById(beerId)) {

            val auth = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerDeletedEvent(Beer.builder().id(beerId).build(), auth));

            beerRepository.deleteById(beerId);
            return true;
        }

        return false;
    }

    @Override
    public Optional<BeerDTO> patchUpdate(UUID beerId, BeerDTO beer) {

        clearCache(beerId);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())) {
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null) {
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null) {
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }

            val savedBeer = beerRepository.save(foundBeer);
            val auth = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerPatchedEvent(savedBeer, auth));

            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(savedBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
