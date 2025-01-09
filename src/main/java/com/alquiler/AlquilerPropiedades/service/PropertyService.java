package com.alquiler.AlquilerPropiedades.service;

import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;

import java.util.List;

public interface PropertyService {


    public List<PropertyDTO> findAllProperties();

    public void saveProperty(Property property);




}
