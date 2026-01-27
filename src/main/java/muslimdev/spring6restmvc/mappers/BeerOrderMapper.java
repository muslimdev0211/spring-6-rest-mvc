package muslimdev.spring6restmvc.mappers;

import muslimdev.spring6restmvc.entities.BeerOrder;
import muslimdev.spring6restmvc.entities.BeerOrderLine;
import muslimdev.spring6restmvc.entities.BeerOrderShipment;
import muslimdev.spring6restmvc.model.BeerOrderDTO;
import muslimdev.spring6restmvc.model.BeerOrderLineDTO;
import muslimdev.spring6restmvc.model.BeerOrderShipmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerOrderMapper {

    BeerOrder beerOrderDTOToBeerOrder(BeerOrderDTO beerOrderDTO);

    BeerOrderDTO beerOrderToBeerOrderDTO(BeerOrder beerOrder);

    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderShipment beerOrderShipmentDTOToBeerOrderShipment(BeerOrderShipmentDTO beerOrderShipmentDTO);

    BeerOrderShipmentDTO beerOrderShipmentToBeerOrderShipmentDTO(BeerOrderShipment beerOrderShipment);

    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderLine beerOrderLineDTOToBeerOrderLine(BeerOrderLineDTO beerOrderLineDTO);

    BeerOrderLineDTO beerOrderLineToBeerOrderLineDTO(BeerOrderLine beerOrderLine);
}
