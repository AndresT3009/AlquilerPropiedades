package com.alquiler.AlquilerPropiedades.infrastructure.service.implementation;

import com.alquiler.AlquilerPropiedades.domain.models.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.infrastructure.service.ClientService;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.ClientException;
import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    protected static final Logger logger = LoggerFactory.getLogger(ClientServiceImplement.class);

    @Override
    public List<ClientDTO> findAllClients() throws ClientException {
        try {
            return clientRepository.findAll().stream()
                    .map(client -> new ClientDTO(client)).collect(Collectors.toList());

        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all clients" + e);
        }
    }

    @Override
    public List<ClientDTO> getclientDTO() throws ClientException {
        try {
            return clientRepository.findAll().stream()
                    .map(client -> new ClientDTO(client)).collect(Collectors.toList());
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding client" + e);
        }
    }

    @Override
    public void saveClient(Client client) {
        try {
            clientRepository.save(client);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error saving client with document {} " + client.getDocument());
        }
    }

    @Override
    public Optional<Client> findByDocument(Long document) {
        try {
            return clientRepository.findByDocument(document);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding client with document {} " + document);
        }
    }
}
