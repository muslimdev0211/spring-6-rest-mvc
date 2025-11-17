package muslimdev.spring6restmvc.Services;

import lombok.extern.slf4j.Slf4j;
import muslimdev.spring6restmvc.model.ClientDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    Map<UUID, ClientDTO> clientMap;

    public ClientServiceImpl() {
        this.clientMap = new HashMap<>();

        ClientDTO client = ClientDTO.builder()
                .id(UUID.randomUUID())
                .clientName("Rajabboy")
                .version("18.6")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        ClientDTO client1 = ClientDTO.builder()
                .id(UUID.randomUUID())
                .clientName("Zikrulla")
                .version("22.2")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        ClientDTO client2 = ClientDTO.builder()
                .id(UUID.randomUUID())
                .clientName("Sardorbek")
                .version("23")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        clientMap.put(client.getId(), client);
        clientMap.put(client1.getId(), client1);
        clientMap.put(client2.getId(), client2);

    }

    @Override
    public List<ClientDTO> getClientList() {

        return new ArrayList<>(clientMap.values());
    }

    @Override
    public Optional<ClientDTO> getClientId(UUID id) {
        log.debug("Get Beer id in Service was called" + id);
        return Optional.of(clientMap.get(id));
    }

    @Override
    public ClientDTO saveNewClient(ClientDTO client) {
        ClientDTO client1 = ClientDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .clientName(client.getClientName())
                .version(client.getVersion())
                .build();

        clientMap.put(client1.getId(), client1);

        return client1;
    }

    @Override
    public Optional<ClientDTO> updateClientId(UUID clientId, ClientDTO client) {
        ClientDTO exciting = clientMap.get(clientId);
        exciting.setClientName(client.getClientName());
        exciting.setVersion(client.getVersion());

        clientMap.put(clientId, exciting);

        return Optional.of(exciting);

    }

    @Override
    public Boolean deleteById(UUID clientId) {
        clientMap.remove(clientId);
        return true;
    }

    @Override
    public void patchById(UUID clienId, ClientDTO client) {
        ClientDTO exciting = clientMap.get(clienId);

        if (StringUtils.hasText(client.getClientName())){
            exciting.setClientName(client.getClientName());
        }

    }
}
