package muslimdev.spring6restmvc.controller;

import lombok.RequiredArgsConstructor;
import muslimdev.spring6restmvc.Services.BeerOrderService;
import muslimdev.spring6restmvc.entities.BeerOrder;
import muslimdev.spring6restmvc.model.BeerOrderCreateDTO;
import muslimdev.spring6restmvc.model.BeerOrderDTO;
import muslimdev.spring6restmvc.model.BeerOrderUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BeerOrderController {

    public static final String BEER_ORDER_PATH = "/api/v1/beerOrder";
    public static final String BEER_ORDER_PATH_ID = BEER_ORDER_PATH + "/{beerOrderId}";

    private final BeerOrderService beerOrderService;

    @GetMapping(BEER_ORDER_PATH_ID)
    public BeerOrderDTO getById(@PathVariable UUID beerOrderId){
        return beerOrderService.getById(beerOrderId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_ORDER_PATH)
    public Page<BeerOrderDTO> listOrders(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return beerOrderService.listOrders(pageNumber, pageSize);
    }

    @PostMapping(BEER_ORDER_PATH)
    public ResponseEntity<Void> createOrder(@RequestBody BeerOrderCreateDTO beerOrderCreateDTO){
        BeerOrder savedOrder = beerOrderService.createOrder(beerOrderCreateDTO);

        return ResponseEntity.created(URI.create(BEER_ORDER_PATH + "/" + savedOrder.getId().toString())).build();
    }

    @PutMapping(BEER_ORDER_PATH_ID)
    public ResponseEntity<BeerOrderDTO> updateById(@PathVariable UUID beerOrderId,
                                           @RequestBody BeerOrderUpdateDTO beerOrderUpdateDTO){
        return ResponseEntity.ok(beerOrderService.updateOrder(beerOrderId, beerOrderUpdateDTO));

    }

    @DeleteMapping(BEER_ORDER_PATH_ID)
    public ResponseEntity<Void> deleteBeerOrder(@PathVariable UUID beerOrderId){
        beerOrderService.deleteBeerOrder(beerOrderId);
        return ResponseEntity.noContent().build();
    }

}
