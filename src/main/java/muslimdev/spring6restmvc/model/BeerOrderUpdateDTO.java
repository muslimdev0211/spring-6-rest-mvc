package muslimdev.spring6restmvc.model;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class BeerOrderUpdateDTO {

    private String clientRef;

    @NotNull
    private UUID clientId;

    private Set<BeerOrderLineUpdateDTO> beerOrderLines;

    private BeerOrderShipmentUpdateDTO beerOrderSHipment;

}
