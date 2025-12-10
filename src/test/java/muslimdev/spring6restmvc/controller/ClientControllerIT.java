package muslimdev.spring6restmvc.controller;

import muslimdev.spring6restmvc.entities.Client;
import muslimdev.spring6restmvc.mappers.ClientMapper;
import muslimdev.spring6restmvc.model.ClientDTO;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientControllerIT {
    @Autowired
    ClientController clientController;

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientMapper clientMapper;


    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () ->{
            clientController.deleteById(UUID.randomUUID());

        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteByIdFound() {
        Client client = clientRepository.findAll().get(0);

        ResponseEntity responseEntity = clientController.deleteById(client.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(clientRepository.findById(client.getId())).isEmpty();
    }




    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            clientController.updateById(UUID.randomUUID(), ClientDTO.builder().build());
        });
    }

    @Test
    void updateExcitingClient() {
        Client client = clientRepository.findAll().get(0);
        ClientDTO clientDTO = clientMapper.clientToClientDto(client);

        clientDTO.setId(null);
        clientDTO.setVersion(null);

        final String updateClientName = "Update Name";
        clientDTO.setClientName(updateClientName);

        ResponseEntity responseEntity = clientController.updateById(client.getId(), clientDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Client updateClient = clientRepository.findById(client.getId()).get();
        assertThat(updateClient.getClientName()).isEqualTo(updateClientName);

    }

    @Rollback
    @Transactional
    @Test
    void saveNewClient() {
        ClientDTO clientDTO = ClientDTO.builder()
                .clientName("TEST")
                .build();

        ResponseEntity responseEntity = clientController.handlePost(clientDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUid = UUID.fromString(location[4]);

        Client client = clientRepository.findById(savedUUid).get();
        assertThat(client).isNotNull();
    }

    @Test
    void testIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            clientController.getById(UUID.randomUUID());
        });
    }

    @Test
    void testGetClientId() {
        Client client = clientRepository.findAll().get(0);

        ClientDTO clientDTO = clientController.getById(client.getId());

        assertThat(clientDTO).isNotNull();
    }

    @Test
    void testGetClients() {
        List<ClientDTO> dtos = clientController.getAllClients();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Transactional
    @Rollback
    @Test
    void testEmptyList() {
        clientRepository.deleteAll();

        List<ClientDTO> dtos = clientController.getAllClients();
        assertThat(dtos.size()).isEqualTo(0);
    }
}