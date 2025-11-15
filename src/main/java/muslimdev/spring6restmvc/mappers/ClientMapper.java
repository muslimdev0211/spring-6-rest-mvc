package muslimdev.spring6restmvc.mappers;

import muslimdev.spring6restmvc.entities.Beer;
import muslimdev.spring6restmvc.entities.Client;
import muslimdev.spring6restmvc.model.BeerDTO;
import muslimdev.spring6restmvc.model.ClientDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ClientMapper {

    Client clientDtoToClient(ClientDTO dto);

    ClientDTO clientToClientDto(Client client);

}
