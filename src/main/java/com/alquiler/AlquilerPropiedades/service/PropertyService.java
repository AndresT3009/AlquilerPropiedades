package com.alquiler.AlquilerPropiedades.service;

import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyService {

    public boolean isAllowedLocation(String ubicacion);

    public boolean citiesMinValueMortgage(String ubicacion);

    public List<PropertyDTO> findAllProperties();

    public void saveProperty(Property property);

    public List<PropertyDTO> findByValue(int min,int max);

    public Optional<Property> findByPropertyName(String propertyName);
    public Optional<Property> findByPropertyId(Long id);

    public void modifyDeletedValue(String propertyName);


}
