package muslimdev.spring6restmvc.Services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import muslimdev.spring6restmvc.controller.NotFoundException;
import muslimdev.spring6restmvc.entities.BeerOrder;
import muslimdev.spring6restmvc.entities.BeerOrderLine;
import muslimdev.spring6restmvc.entities.BeerOrderShipment;
import muslimdev.spring6restmvc.entities.Client;
import muslimdev.spring6restmvc.mappers.BeerOrderMapper;
import muslimdev.spring6restmvc.model.BeerOrderCreateDTO;
import muslimdev.spring6restmvc.model.BeerOrderDTO;
import muslimdev.spring6restmvc.model.BeerOrderUpdateDTO;
import muslimdev.spring6restmvc.repositories.BeerOrderRepository;
import muslimdev.spring6restmvc.repositories.BeerRepository;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerOrderServiceImplJPA implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final ClientRepository clientRepository;
    private final BeerRepository beerRepository;

    @Override
    public Optional<BeerOrderDTO> getById(UUID beerOrderId) {
        return Optional.ofNullable(beerOrderMapper.beerOrderToBeerOrderDTO(beerOrderRepository.findById(beerOrderId).orElse(null)));
    }

    @Override
    public Page<BeerOrderDTO> listOrders(Integer pageNumber, Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 25;
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return beerOrderRepository.findAll(pageRequest).map(beerOrderMapper::beerOrderToBeerOrderDTO);
    }

    @Override
    public BeerOrder createOrder(BeerOrderCreateDTO beerOrderCreateDTO) {
        Client client = clientRepository.findById(beerOrderCreateDTO.getClientId()).orElseThrow(NotFoundException::new);

        Set<BeerOrderLine> beerOrderLines = new HashSet<>();

        beerOrderCreateDTO.getBeerOrderLines().forEach(beerOrderLine -> {
            beerOrderLines.add(BeerOrderLine.builder()
                    .beer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new))
                    .orderQuantity(beerOrderLine.getOrderQuantity())
                    .build());
        });

        return beerOrderRepository.save(BeerOrder.builder()
                .client(client)
                .beerOrderLines(beerOrderLines)
                .clientRef(beerOrderCreateDTO.getClientRef())
                .build());
    }

    @Override
    public BeerOrderDTO updateOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO) {
        val order = beerOrderRepository.findById(beerOrderId).orElseThrow(NotFoundException::new);
        order.setClient(clientRepository.findById(beerOrderUpdateDTO.getClientId()).orElseThrow(NotFoundException::new));
        order.setClientRef(beerOrderUpdateDTO.getClientRef());

        beerOrderUpdateDTO.getBeerOrderLines().forEach(beerOrderLine -> {
            if (beerOrderLine.getBeerId() != null) {

                val foundLine = order.getBeerOrderLines().stream()
                        .filter(beerOrderLine1 -> beerOrderLine1.getId().equals(beerOrderLine.getId()))
                        .findFirst().orElseThrow(NotFoundException::new);
                foundLine.setBeer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new));
                foundLine.setOrderQuantity(beerOrderLine.getOrderQuantity());
                foundLine.setQuantityAllocated(beerOrderLine.getQuantityAllocated());
            } else {
                order.getBeerOrderLines().add(BeerOrderLine.builder()
                        .beer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new))
                        .orderQuantity(beerOrderLine.getOrderQuantity())
                        .quantityAllocated(beerOrderLine.getQuantityAllocated())
                        .build());
            }
        });

        if (beerOrderUpdateDTO.getBeerOrderSHipment() != null &&
                beerOrderUpdateDTO.getBeerOrderSHipment().getTrackingNumber() != null){
            if (order.getBeerOrderShipment() == null){
                order.setBeerOrderShipment(BeerOrderShipment.builder().trackingNumber(beerOrderUpdateDTO.getBeerOrderSHipment().getTrackingNumber()).build());
            }else {
                order.getBeerOrderShipment().setTrackingNumber(beerOrderUpdateDTO.getBeerOrderSHipment().getTrackingNumber());
            }
        }
        return beerOrderMapper.beerOrderToBeerOrderDTO(beerOrderRepository.save(order));
    }

    @Override
    public void deleteBeerOrder(UUID beerOrderId) {
        if (beerOrderRepository.existsById(beerOrderId)){
            beerOrderRepository.deleteById(beerOrderId);
        } else {
            throw new NotFoundException();
        }
    }
}
