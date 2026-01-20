package muslimdev.spring6restmvc.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muslimdev.spring6restmvc.mappers.ClientMapper;
import muslimdev.spring6restmvc.model.ClientDTO;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ClientServiceJPA implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "clientList")
    @Override
    public List<ClientDTO> getClientList() {
        log.info("client List in Cache");
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "clientCache")
    @Override
    public Optional<ClientDTO> getClientId(UUID uuid) {
        log.info("Client cache - in  service");
        return Optional.ofNullable(clientMapper.clientToClientDto(clientRepository.findById(uuid).orElse(null)));
    }

    @Override
    public ClientDTO saveNewClient(ClientDTO client) {
        if (cacheManager.getCache("clientList") != null){
            cacheManager.getCache("clientList").clear();
        }
        return clientMapper.clientToClientDto(clientRepository.save(clientMapper.clientDtoToClient(client)));
    }

    @Override
    public Optional<ClientDTO> updateClientId(UUID clientId, ClientDTO client) {
        clearCache(clientId);
        AtomicReference<Optional<ClientDTO>> atomicReference = new AtomicReference<>();

        clientRepository.findById(clientId).ifPresentOrElse(foundClient -> {
            foundClient.setClientName(client.getClientName());

            atomicReference.set(Optional.of(clientMapper
                    .clientToClientDto(clientRepository.save(foundClient))));
        } ,() -> {
  atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    private void clearCache(UUID clientId){
        if (cacheManager.getCache("clientList") != null){
            cacheManager.getCache("clientList").clear();
        }
        if (cacheManager.getCache("clientCache") != null){
            cacheManager.getCache("clientCache").evict(clientId);
        }
    }

    @Override
    public Boolean deleteById(UUID clientId) {
        clearCache(clientId);
        if (clientRepository.existsById(clientId)){
            clientRepository.deleteById(clientId);
            return true;
        }

        return false;
    }

    @Override
    public void patchById(UUID clienId, ClientDTO client) {

    }
}
