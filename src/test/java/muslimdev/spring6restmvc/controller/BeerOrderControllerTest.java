package muslimdev.spring6restmvc.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import muslimdev.spring6restmvc.entities.BeerOrderLine;
import muslimdev.spring6restmvc.model.*;
import muslimdev.spring6restmvc.repositories.BeerOrderRepository;
import muslimdev.spring6restmvc.repositories.BeerRepository;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerOrderControllerTest {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Transactional
    @Test
    void testDeleteBeerOrder() throws Exception {
        val beerOrder = beerOrderRepository.findAll().get(0);
        mockMvc.perform(delete(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isNoContent());

        assertTrue(beerOrderRepository.findById(beerOrder.getId()).isEmpty());

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isNotFound());

    }

    @Transactional
    @Test
    void testUpdateBeerOrder() throws Exception{
        val beerOrder = beerOrderRepository.findAll().get(0);

        Set<BeerOrderLineUpdateDTO> lines = new HashSet<>();

        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
            lines.add(BeerOrderLineUpdateDTO.builder()
                            .id(beerOrderLine.getId())
                            .beerId(beerOrderLine.getBeer().getId())
                            .orderQuantity(beerOrderLine.getOrderQuantity())
                            .quantityAllocated(beerOrderLine.getQuantityAllocated())
                    .build());
        });

        val beerOrderUpdateDTO = BeerOrderUpdateDTO.builder()
                .clientId(beerOrder.getClient().getId())
                .clientRef("TestRef")
                .beerOrderLines(lines)
                .beerOrderSHipment(BeerOrderShipmentUpdateDTO.builder()
                        .trackingNumber("231313")
                        .build())
                .build();

        mockMvc.perform(put(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(BeerControllerTest.jwtRequestPostProcessor)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(beerOrderUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientRef", is("TestRef")));

    }

    @Test
    void testCreateBeerOrder() throws Exception {
        val client = clientRepository.findAll().get(0);
        val beer = beerRepository.findAll().get(0);
//        ObjectMapper objectMapper = new ObjectMapper();

        val beerOrderCreatedDto = BeerOrderCreateDTO.builder()
                .clientId(client.getId())
                .beerOrderLines(Set.of(BeerOrderLineCreateDTO.builder()
                                .beerId(beer.getId())
                                .orderQuantity(1)
                        .build()))
                .build();

        mockMvc.perform(post(BeerOrderController.BEER_ORDER_PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(beerOrderCreatedDto))
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));


    }

    @Test
    void testListBeerOrder() throws Exception {
        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH)
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", greaterThan(0)));
    }

    @Test
    void testGetBeerOrderById() throws Exception {
        val beerOrder = beerOrderRepository.findAll().get(0);
        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beerOrder.getId().toString())));
    }
}