package com.alquiler.AlquilerPropiedades.service.implementation;

import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;
import com.alquiler.AlquilerPropiedades.jpa.repository.PropertyRepository;
import com.alquiler.AlquilerPropiedades.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImplement implements PropertyService {

    @Autowired
    PropertyRepository propertyRepository;


    private boolean esUbicacionPermitida(String ubicacion) {
        List<String> ciudadesPermitidas = Arrays.asList("bogota", "bogotá", "medellin","medellín", "cali", "cartagena");
        String ubicacionMinusculas = ubicacion.toLowerCase();
        return ciudadesPermitidas.stream()
                .anyMatch(ciudad -> ciudad.toLowerCase().equals(ubicacionMinusculas));
    }

    @Override
    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    @Override
    public List<PropertyDTO> findAllProperties() {
        return propertyRepository.findAll().stream().map(property -> new PropertyDTO(property)).collect(Collectors.toList());
    }
}
