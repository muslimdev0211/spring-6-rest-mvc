package muslimdev.spring6restmvc.Services;

import lombok.extern.slf4j.Slf4j;
import muslimdev.spring6restmvc.model.BeerDTO;
import muslimdev.spring6restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Qibray")
                .beerStyle(BeerStyle.GOSE)
                .price(new BigDecimal("423.2"))
                .upc("232")
                .quantityOnHand(121)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(2)
                .beerName("Sarbast")
                .beerStyle(BeerStyle.ALE)
                .price(new BigDecimal("432.3"))
                .upc("42")
                .quantityOnHand(32)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(3)
                .beerName("Baltika")
                .beerStyle(BeerStyle.LAGER)
                .price(new BigDecimal("32.3"))
                .upc("323")
                .quantityOnHand(323)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())

                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(beerMap.values()));
    }

    @Override
    public Optional<BeerDTO> getBeerId(UUID id) {

        log.debug("Get Beer id in Service was called" + id.toString());

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .version(1)
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .build();

        beerMap.put(beer1.getId(), beer1);

        return beer1;
    }

    @Override
    public Optional<BeerDTO> updateById(UUID id, BeerDTO beer) {
        BeerDTO exciting = beerMap.get(id);
        exciting.setBeerName(beer.getBeerName());
        exciting.setBeerStyle(beer.getBeerStyle());
        exciting.setUpc(beer.getUpc());
        exciting.setPrice(beer.getPrice());
        exciting.setVersion(beer.getVersion());

        beerMap.put(id, exciting);

        return Optional.of(exciting);

    }

    @Override
    public Boolean deleteById(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    @Override
    public Optional<BeerDTO> patchUpdate(UUID beerId, BeerDTO beer) {

        BeerDTO exciting = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())) {
            exciting.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            exciting.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            exciting.setPrice(beer.getPrice());
        }
        if (StringUtils.hasText(beer.getUpc())) {
            exciting.setUpc(beer.getUpc());
        }

        if (beer.getQuantityOnHand() != null) {
            exciting.setQuantityOnHand(beer.getQuantityOnHand());
        }
        return Optional.of(exciting);
    }
}
