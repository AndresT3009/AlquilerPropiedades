package com.alquiler.AlquilerPropiedades.service.implementation;

import com.alquiler.AlquilerPropiedades.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Client;
import com.alquiler.AlquilerPropiedades.jpa.repository.ClientRepository;
import com.alquiler.AlquilerPropiedades.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<ClientDTO> findAllClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public List<ClientDTO> getclientDTO(){
     return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }

    @Override
    public Optional<ClientDTO> findByDocument (Long document){
        return clientRepository.findByDocument(document).map(ClientDTO::new);
    }

}
