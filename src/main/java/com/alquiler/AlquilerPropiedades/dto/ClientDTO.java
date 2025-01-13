package com.alquiler.AlquilerPropiedades.dto;

import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Client;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ClientDTO {

    private Long client_id;

    private String fisrtName;

    private String secondName;

    private String surName;

    private String phoneNumber;

    private Long document;

    private Set<PropertyDTO> properties = new HashSet<>();

    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        this.client_id = client.getId();
        this.fisrtName = client.getFisrtName();
        this.secondName = client.getSecondName();
        this.surName = client.getSurName();
        this.phoneNumber = client.getPhoneNumber();
        this.document = client.getDocument();
        this.properties=client.getProperties().stream().map(property -> new PropertyDTO(property)).collect(Collectors.toSet());

    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "client_id=" + client_id +
                ", fisrtName='" + fisrtName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surName='" + surName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", document=" + document +
                '}';
    }
}
