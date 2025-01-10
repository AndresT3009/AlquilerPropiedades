package com.alquiler.AlquilerPropiedades.service;

import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;

import java.util.List;

public interface PropertyService {

    public boolean isAllowedLocation(String ubicacion);

    public boolean citiesMinValueMortgage(String ubicacion);

    public List<PropertyDTO> findAllProperties();

    public void saveProperty(Property property);

    public List<PropertyDTO> findByValue(int min,int max);

    public List<PropertyDTO> findByPropertyName(String propertyName);


}
