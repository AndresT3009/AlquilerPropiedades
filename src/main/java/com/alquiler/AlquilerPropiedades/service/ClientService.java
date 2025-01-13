package com.alquiler.AlquilerPropiedades.service;

import com.alquiler.AlquilerPropiedades.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    public List<ClientDTO> findAllClients();
    public List<ClientDTO> getclientDTO();
    public void saveClient(Client client);
    public Optional<ClientDTO> findByDocument(Long document);
}
