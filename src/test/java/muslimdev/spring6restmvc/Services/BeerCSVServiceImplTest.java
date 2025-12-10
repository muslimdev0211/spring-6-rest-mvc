package muslimdev.spring6restmvc.Services;

import muslimdev.spring6restmvc.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BeerCSVServiceImplTest {

    BeerCSVService beerCSVService = new BeerCSVServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beer.csv");

        List<BeerCSVRecord> beerCSVRecords = beerCSVService.convertCSV(file);

        System.out.println(beerCSVRecords.size());

        assertThat(beerCSVRecords.size()).isGreaterThan(0);

    }
}