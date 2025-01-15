package com.alquiler.AlquilerPropiedades.infrastructure.service;

import com.alquiler.AlquilerPropiedades.domain.models.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.PropertyException;
import com.alquiler.AlquilerPropiedades.domain.models.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyService {

    public boolean isAllowedLocation(String ubicacion) throws PropertyException;

    public boolean citiesMinValueMortgage(String ubicacion) throws PropertyException;

    public List<PropertyDTO> findAllProperties() throws PropertyException;

    public void saveProperty(Property property) throws PropertyException;

    public List<PropertyDTO> findByValue(int min, int max) throws PropertyException;

    public Optional<Property> findByPropertyName(String propertyName) throws PropertyException;

    public Optional<Property> findByPropertyId(Long id) throws PropertyException;

    public void modifyDeletedValue(String propertyName) throws PropertyException;
}
