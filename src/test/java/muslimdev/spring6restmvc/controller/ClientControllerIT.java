package muslimdev.spring6restmvc.controller;

import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.entities.Client;
import muslimdev.spring6restmvc.model.ClientDTO;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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