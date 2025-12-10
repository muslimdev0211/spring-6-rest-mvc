package muslimdev.spring6restmvc.bootstrap;

import lombok.RequiredArgsConstructor;
import muslimdev.spring6restmvc.Services.BeerCSVService;
import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.entities.Client;
import muslimdev.spring6restmvc.model.BeerCSVRecord;
import muslimdev.spring6restmvc.model.BeerStyle;
import muslimdev.spring6restmvc.repositories.BeerRepository;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final ClientRepository clientRepository;
    private final BeerCSVService beerCSVService;


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCSVdata();
        loadClientData();

    }


    private void loadCSVdata() throws FileNotFoundException {
        if (beerRepository.count() < 10){
            File file = ResourceUtils.getFile("classpath:csvdata/beer.csv");

            List<BeerCSVRecord> recs = beerCSVService.convertCSV(file);

            recs.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }

    private void loadClientData() {
        if (clientRepository.count() == 0) {
            Client client = Client.builder()
                    .clientName("Rajabboy")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Client client1 = Client.builder()
                    .clientName("Zikrulla")
                    .version(7)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Client client2 = Client.builder()
                    .clientName("Sardorbek")
                    .version(5)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();
            clientRepository.saveAll(Arrays.asList(client, client1, client2));


        }


    }

    private void loadBeerData() {
        if (beerRepository.count() == 0){
            Beer beer1 = Beer.builder()
                    .beerName("Qibray")
                    .beerStyle(BeerStyle.GOSE)
                    .price(new BigDecimal("423.2"))
                    .upc("232")
                    .quantityOnHand(121)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();
            Beer beer2 = Beer.builder()
                    .beerName("Sarbast")
                    .beerStyle(BeerStyle.ALE)
                    .price(new BigDecimal("432.3"))
                    .upc("42")
                    .quantityOnHand(32)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Baltika")
                    .beerStyle(BeerStyle.LAGER)
                    .price(new BigDecimal("32.3"))
                    .upc("323")
                    .quantityOnHand(323)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }
    }


}
