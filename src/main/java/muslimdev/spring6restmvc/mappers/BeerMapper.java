package muslimdev.spring6restmvc.mappers;

import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.entities.BeerAudit;
import muslimdev.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerMapper {

//    @Mapping(source = "uuid", target = "id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "beerOrderLines", ignore = true)
    Beer beerDtoToBeer(BeerDTO dto);

//    @Mapping(source = "id", target = "uuid")
    BeerDTO beerToBeerDto(Beer  beer);

    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "auditEventType", ignore = true)
    @Mapping(target = "principalName", ignore = true)
    BeerAudit beerToBeerAudit(Beer beer);
}
