package muslimdev.spring6restmvc.Services;

import lombok.RequiredArgsConstructor;
import muslimdev.spring6restmvc.mappers.ClientMapper;
import muslimdev.spring6restmvc.model.ClientDTO;
import muslimdev.spring6restmvc.repositories.ClientRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class ClientServiceJPA implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDTO> getClientList() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::clientToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO> getClientId(UUID uuid) {
        return Optional.ofNullable(clientMapper.clientToClientDto(clientRepository.findById(uuid).orElse(null)));
    }

    @Override
    public ClientDTO saveNewClient(ClientDTO client) {
        return clientMapper.clientToClientDto(clientRepository.save(clientMapper.clientDtoToClient(client)));
    }

    @Override
    public Optional<ClientDTO> updateClientId(UUID clientId, ClientDTO client) {
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

    @Override
    public void deleteById(UUID clientId) {

    }

    @Override
    public void patchById(UUID clienId, ClientDTO client) {

    }
}
