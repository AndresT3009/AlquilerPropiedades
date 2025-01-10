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

    private boolean verifyCity(String cityLocation, List<String> allowedCities) {
        String cityLower = cityLocation.toLowerCase();
        return allowedCities.stream()
                .anyMatch(city -> city.toLowerCase().equals(cityLower));
    }

    @Override
    public boolean isAllowedLocation(String cityLocation) {
        List<String> allowedCities = Arrays.asList("medellin", "medellín", "cartagena","bogota", "bogotá", "cali");
        return verifyCity(cityLocation, allowedCities);
    }

    @Override
    public boolean citiesMinValueMortgage(String ubicacion) {
        List<String> allowedCities = Arrays.asList("bogota", "bogotá", "cali");
        return verifyCity(ubicacion, allowedCities);
    }

    @Override
    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    @Override
    public List<PropertyDTO> findAllProperties() {
        return propertyRepository.findAll().stream().map(property -> new PropertyDTO(property)).collect(Collectors.toList());
    }

    @Override
    public List<PropertyDTO> findByValue(int min, int max){
        return propertyRepository.findByMortgageValue(min, max).stream().map(property -> new PropertyDTO(property)).collect(Collectors.toList());
    }

    @Override
    public  List<PropertyDTO>findByPropertyName(String propertyName){
        return propertyRepository.findByPropertyName(propertyName).stream().map(property -> new PropertyDTO(property)).collect(Collectors.toList());
    }
}
