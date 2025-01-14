package com.alquiler.AlquilerPropiedades.infrastructure.service;

import com.alquiler.AlquilerPropiedades.domain.models.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.ClientException;
import com.alquiler.AlquilerPropiedades.domain.models.Client;
import java.util.List;
import java.util.Optional;

public interface ClientService {

    public List<ClientDTO> findAllClients() throws ClientException;
    public List<ClientDTO> getclientDTO() throws ClientException;
    public void saveClient(Client client) throws ClientException;
    public Optional<Client> findByDocument(Long document) throws ClientException;
}
