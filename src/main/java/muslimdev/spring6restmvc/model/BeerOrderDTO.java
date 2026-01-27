package muslimdev.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;
import muslimdev.spring6restmvc.entities.BeerOrderLine;
import muslimdev.spring6restmvc.entities.BeerOrderShipment;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class BeerOrderDTO {

    private UUID id;
    private Long version;
    private Timestamp createdDate;
    private Timestamp LastModifiedDate;
    private String clientRef;
    private ClientDTO client;

    private Set<BeerOrderLineDTO> beerOrderLines;

    private BeerOrderShipmentDTO beerOrderShipment;
}
