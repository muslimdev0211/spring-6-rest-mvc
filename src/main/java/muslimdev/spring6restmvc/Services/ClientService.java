package muslimdev.spring6restmvc.Services;

import muslimdev.spring6restmvc.model.ClientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {


    List<ClientDTO> getClientList();
    Optional<ClientDTO> getClientId(UUID uuid);

    ClientDTO saveNewClient(ClientDTO client);

    void updateClientId(UUID clientId, ClientDTO client);

    void deleteById(UUID clientId);

    void patchById(UUID clienId, ClientDTO client);
}
