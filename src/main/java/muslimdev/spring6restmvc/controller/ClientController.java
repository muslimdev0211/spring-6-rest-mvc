package muslimdev.spring6restmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import muslimdev.spring6restmvc.Services.ClientService;
import muslimdev.spring6restmvc.model.ClientDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ClientController {

    private final ClientService clientService;

    public static final String CLIENT_PATH = "/api/v1/clients";
    public static final String CLIENT_PATH_ID = CLIENT_PATH + "/{clientId}";

    @PutMapping(CLIENT_PATH_ID)
    public ResponseEntity updateById(@PathVariable("clientId") UUID clientId, @RequestBody ClientDTO client) {

       if (clientService.updateClientId(clientId, client).isEmpty()){
           throw new NotFoundException();
       };

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(CLIENT_PATH)
    public List<ClientDTO> getAllClients() {
        return clientService.getClientList();
    }

    @PostMapping(CLIENT_PATH)
    public ResponseEntity handlePost(ClientDTO client) {
        ClientDTO saveClient = clientService.saveNewClient(client);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/clients/" + saveClient.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(CLIENT_PATH_ID)
    public ClientDTO getById(@PathVariable("clientId") UUID clientId) {
        log.debug("Get Client By Id - in controller");

        return clientService.getClientId(clientId).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping(CLIENT_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("clientId") UUID clientId) {
        if (!clientService.deleteById(clientId)){
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CLIENT_PATH_ID)
    public ResponseEntity patchClientById(@PathVariable("clientId") UUID clienId, @RequestBody ClientDTO client) {

        clientService.patchById(clienId, client);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
