package com.alquiler.AlquilerPropiedades.infrastructure.service.implementation;

import com.alquiler.AlquilerPropiedades.domain.models.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.ClientException;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.PropertyException;
import com.alquiler.AlquilerPropiedades.domain.models.Property;
import com.alquiler.AlquilerPropiedades.domain.repository.PropertyRepository;
import com.alquiler.AlquilerPropiedades.infrastructure.service.PropertyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImplement implements PropertyService {

    @Autowired
    PropertyRepository propertyRepository;

    private boolean verifyCity(String cityLocation, List<String> allowedCities) throws PropertyException {
        try {
            String cityLower = cityLocation.toLowerCase();
            return allowedCities.stream()
                    .anyMatch(city -> city.toLowerCase().equals(cityLower));
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error verifying city" + e);
        }
    }

    @Override
    public boolean isAllowedLocation(String cityLocation) throws PropertyException {
        try {
            List<String> allowedCities = Arrays.asList("medellin", "medellín", "cartagena", "bogota", "bogotá", "cali");
            return verifyCity(cityLocation, allowedCities);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error verifying allowed cities" + e);
        }
    }

    @Override
    public boolean citiesMinValueMortgage(String ubicacion) throws PropertyException {
        try {
            List<String> allowedCities = Arrays.asList("bogota", "bogotá", "cali");
            return verifyCity(ubicacion, allowedCities);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error verifying minimun values cities" + e);
        }
    }

    @Override
    public void saveProperty(Property property) throws PropertyException {
        try {
            propertyRepository.save(property);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error saving property" + e);
        }
    }

    @Override
    public List<PropertyDTO> findAllProperties() throws PropertyException {
        try {
            return propertyRepository.findAll()
                    .stream()
                    .map(property -> new PropertyDTO(property))
                    .collect(Collectors.toList());
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding all properties" + e);
        }
    }

    @Override
    public List<PropertyDTO> findByValue(int min, int max) throws PropertyException {
        try {
            return propertyRepository.findByMortgageValue(min, max)
                    .stream()
                    .map(property -> new PropertyDTO(property)).collect(Collectors.toList());
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding value with the given ranges" + e);
        }
    }

    @Override
    public Optional<Property> findByPropertyName(String propertyName) throws PropertyException {
        try {
            return propertyRepository.findByPropertyName(propertyName);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding property by name" + e);
        }
    }

    @Override
    public Optional<Property> findByPropertyId(Long id) throws PropertyException {
        try {
            return propertyRepository.findByPropertyId(id);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding property by id" + e);
        }
    }

    @Transactional
    public void modifyDeletedValue(String propertyName) throws PropertyException {
        try {
            propertyRepository.modifyDeletedValue(propertyName);
        } catch (ClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting property from the system" + e);
        }
    }

    @Override
    public PropertyDTO updateProperty(String propertyName, String newPropertyName, String city, boolean available, String address, int mortgageValue, String image) throws PropertyException {
        Optional<Property> optionalProperty = propertyRepository.findByPropertyName(propertyName);
        if (optionalProperty.isEmpty()) {
            throw new PropertyException("Property not found");
        }
        Property property = optionalProperty.get();
        property.setPropertyName(newPropertyName);
        property.setCity(city);
        property.setAddress(address);
        property.setMortgageValue(mortgageValue);
        property.setImage(image);

        Property updatedProperty = propertyRepository.save(property);

        return new PropertyDTO(updatedProperty);
    }
}
