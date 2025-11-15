package muslimdev.spring6restmvc.mappers;

import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerMapper {

//    @Mapping(source = "uuid", target = "id")
    Beer beerDtoToBeer(BeerDTO dto);

//    @Mapping(source = "id", target = "uuid")
    BeerDTO beerToBeerDto(Beer  beer);
}
