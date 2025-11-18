package muslimdev.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import muslimdev.spring6restmvc.Services.ClientService;
import muslimdev.spring6restmvc.Services.ClientServiceImpl;
import muslimdev.spring6restmvc.model.ClientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {


    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ClientService clientService;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<ClientDTO> clientArgumentCaptor;

    ClientServiceImpl clientServiceImpl;


    @Test
    void getAllClients() throws Exception {
        given(clientService.getClientList()).willReturn(clientServiceImpl.getClientList());

        mockMvc.perform(get(ClientController.CLIENT_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }

    @Test
    void testPatcher() throws Exception {
        ClientDTO client = clientServiceImpl.getClientList().get(1);

        Map<String, Object> clinetMap = new HashMap<>();

        clinetMap.put("clientName", "New Name");

        mockMvc.perform(patch(ClientController.CLIENT_PATH + "/" + client.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinetMap)))
                .andExpect(status().isNoContent());

        verify(clientService).patchById(uuidArgumentCaptor.capture(), clientArgumentCaptor.capture());
        assertThat(client.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(clinetMap.get("clientName")).isEqualTo(clientArgumentCaptor.getValue().getClientName());


    }





    @Test
    void getById() throws Exception {
        ClientDTO testClient = clientServiceImpl.getClientList().get(0);

        given(clientService.getClientId(testClient.getId())).willReturn(Optional.of(testClient));

        mockMvc.perform(get( ClientController.CLIENT_PATH + "/" + testClient.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testClient.getId().toString())))
                .andExpect(jsonPath("$.clientName", is(testClient.getClientName().toString())));

    }

    @BeforeEach
    void setUp() {
        clientServiceImpl = new ClientServiceImpl();
    }

    @Test
    void testCreateNewClient() throws Exception {
        ClientDTO client = clientServiceImpl.getClientList().get(0);
        client.setId(null);
        client.setVersion(null);

        given(clientService.saveNewClient(any(ClientDTO.class))).willReturn(clientServiceImpl.getClientList().get(1));

        mockMvc.perform(post(ClientController.CLIENT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));


        System.out.println(objectMapper.writeValueAsString(client));


    }

    @Test
    void TestDeleteClient() throws Exception {
        ClientDTO client = clientServiceImpl.getClientList().get(0);
        given(clientService.deleteById(any())).willReturn(true);
        mockMvc.perform(delete(ClientController.CLIENT_PATH + "/" + client.getId())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(clientService).deleteById(argumentCaptor.capture());

        assertThat(client.getId()).isEqualTo(argumentCaptor.getValue());
    }

    @Test
    void TestUpdateClient() throws Exception {
        ClientDTO client = clientServiceImpl.getClientList().get(0);
        given(clientService.updateClientId(any(), any())).willReturn(Optional.of(client));
        mockMvc.perform(put(ClientController.CLIENT_PATH + "/" + client.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isNoContent());

        verify(clientService).updateClientId(any(UUID.class), any(ClientDTO.class));
    }

    @Test
    void getByIdNotFound() throws Exception {

        given(clientService.getClientId(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(ClientController.CLIENT_PATH_ID, UUID.randomUUID())).andExpect(status().isNotFound());
    }


}