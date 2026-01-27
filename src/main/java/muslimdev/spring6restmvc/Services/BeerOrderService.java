package muslimdev.spring6restmvc.Services;

import muslimdev.spring6restmvc.entities.BeerOrder;
import muslimdev.spring6restmvc.model.BeerOrderCreateDTO;
import muslimdev.spring6restmvc.model.BeerOrderDTO;
import muslimdev.spring6restmvc.model.BeerOrderUpdateDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerOrderService {

    Optional<BeerOrderDTO> getById(UUID beerOrderId);

    Page<BeerOrderDTO> listOrders(Integer pageNumber, Integer pageSize);

    BeerOrder createOrder(BeerOrderCreateDTO beerOrderCreateDTO);

    BeerOrderDTO updateOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO);

    void deleteBeerOrder(UUID beerOrderId);
}
