package com.alquiler.AlquilerPropiedades.controllers;

import com.alquiler.AlquilerPropiedades.domain.models.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.domain.models.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.ErrorResponse;
import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.models.Property;
import com.alquiler.AlquilerPropiedades.infrastructure.service.ClientService;
import com.alquiler.AlquilerPropiedades.infrastructure.service.PropertyService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class PropertyController {
    @Autowired
    PropertyService propertyService;
    @Autowired
    ClientService clientService;

    @GetMapping("/properties/all")
    public ResponseEntity<?> findAll() {
        try {
            List<PropertyDTO> properties = propertyService.findAllProperties();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Message", "Request successful")
                    .body(properties);
        } catch (Exception e) {
            log.error("Error retrieving properties: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while retrieving properties.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/properties/search")
    public ResponseEntity<?> searchProperty(
            @RequestParam int minValue,
            @RequestParam int maxValue
    ) {
        try {
            if (minValue <= 0 || maxValue <= 0 || minValue > maxValue) {
                return new ResponseEntity<>("Invalid range values", HttpStatus.BAD_REQUEST);
            }
            List<PropertyDTO> properties = propertyService.findByValue(minValue, maxValue);
            if (properties.isEmpty()) {
                return new ResponseEntity<>("No properties found in the given range", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.OK).header("Message", "Request succesfull").body(properties);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error searching property: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while searching for the property.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/properties/save")
    public ResponseEntity<?> insertProperty(
            @RequestParam String propertyName,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam int mortgageValue,
            @RequestParam String image
    ) {
        try {
            if (propertyName.isEmpty()) {
                return new ResponseEntity<>("You must to specify property Name", HttpStatus.FORBIDDEN);
            }
            if (city.isEmpty()) {
                return new ResponseEntity<>("You must to specify city", HttpStatus.FORBIDDEN);
            } else if (propertyService.citiesMinValueMortgage(city)) {
                if (mortgageValue <= 2000000) {
                    return new ResponseEntity<>("Mortgage value must be greather than $2000000 in Cali and Bogotá", HttpStatus.FORBIDDEN);
                }
            }
            if (address.isEmpty()) {
                return new ResponseEntity<>("You must to specify address", HttpStatus.FORBIDDEN);
            }
            if (mortgageValue <= 0) {
                return new ResponseEntity<>("You must to enter a value greather than 0", HttpStatus.FORBIDDEN);
            }
            if (image.isEmpty()) {
                return new ResponseEntity<>("You must to enter a valid url", HttpStatus.FORBIDDEN);
            }
            boolean available = true;
            boolean isDeleted = false;
            LocalDate date = LocalDate.now();

            Optional<Property> property = propertyService.findByPropertyName(propertyName);
            if (property.isPresent()) {
                return new ResponseEntity<>("Property name already exist and its avalability is " + property.get().isAvailable(), HttpStatus.CONFLICT);
            } else {
                if (propertyService.isAllowedLocation(city)) {
                    Property newProperty = new Property(propertyName, city, address, available, mortgageValue, image, date, isDeleted);
                    propertyService.saveProperty(newProperty);
                    return ResponseEntity.status(HttpStatus.CREATED).header("Message", "Property created succesfully").body(newProperty);
                } else {
                    return new ResponseEntity<>("Property location doesn't allowed", HttpStatus.NOT_ACCEPTABLE);
                }
            }
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error saving property: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while saving the property.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping("/properties/delete")
    public ResponseEntity<?> searchProperty(
            @RequestParam String propertyName
    ) {
        try {
            if (propertyName.isEmpty()) {
                return new ResponseEntity<>("You must to specify property Name", HttpStatus.FORBIDDEN);
            }
            Optional<Property> property = propertyService.findByPropertyName(propertyName);
            if (property.isPresent()) {
                if (property.get().getDate().plusDays(30).isBefore(LocalDate.now())) {
                    if (!property.get().isAvailable()) {
                        return new ResponseEntity<>("Property cannot be deleted because the currently is reserved", HttpStatus.FORBIDDEN);
                    }
                    propertyService.modifyDeletedValue(propertyName);
                    return ResponseEntity.status(HttpStatus.OK).header("Message", "Property deleted succesfully").body("Property deleted succesfully");
                } else {
                    return new ResponseEntity<>("Property cannot be deleted because the property creation date is less than one month ", HttpStatus.FORBIDDEN);
                }
            }
            return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error deleting property: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while deleting the property.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Transactional
    @PostMapping("/properties/reserve")
    public ResponseEntity<?> reserveProperty(
            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) Long clientDocument) {
        try {
            if (propertyId == null) {
                return new ResponseEntity<>("You must to specify property Id", HttpStatus.FORBIDDEN);
            }
            if (clientDocument == null) {
                return new ResponseEntity<>("You must to specify client document", HttpStatus.FORBIDDEN);
            }
            Optional<Property> property = propertyService.findByPropertyId(propertyId);
            if (property.isEmpty()) {
                return new ResponseEntity<>("Property cannot be reserved, because property does not exist", HttpStatus.FORBIDDEN);
            }
            Optional<Client> client = clientService.findByDocument(clientDocument);
            if (client.isEmpty()) {
                return new ResponseEntity<>("Property cannot be reserved, because client does not exist", HttpStatus.FORBIDDEN);
            }
            Property foundProperty = property.get();
            Client foundClient = client.get();

            if (foundProperty.isAvailable() == false && foundProperty.isDeleted() == true) {
                return new ResponseEntity<>("Property cannot be reserved, because it is not available", HttpStatus.FORBIDDEN);
            }
            foundProperty.setAvailable(false);
            foundProperty.setClient(foundClient);
            foundClient.addProperty(foundProperty);

            propertyService.saveProperty(foundProperty);

            ClientDTO updatedClient = new ClientDTO(foundClient);
            return ResponseEntity.status(HttpStatus.OK).header("Message", "Property reserved succesfully").body(updatedClient);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error reserving property: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while reserve the property.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PatchMapping("/properties/update")
    public ResponseEntity<?> updateProperty(
            @RequestParam String propertyName,
            @RequestParam String newPropertyName,
            @RequestParam String available,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam int mortgageValue,
            @RequestParam String image
    ) {
        try {
            if (propertyName.isEmpty()) {
                return new ResponseEntity<>("You must to specify property Name", HttpStatus.FORBIDDEN);
            }
            if (newPropertyName.isEmpty()) {
                return new ResponseEntity<>("You must to specify new property Name", HttpStatus.FORBIDDEN);
            }
            if (city.isEmpty()) {
                return new ResponseEntity<>("You must to specify city", HttpStatus.FORBIDDEN);
            }
            if (available.isEmpty()) {
                return new ResponseEntity<>("You must to specify avalability", HttpStatus.FORBIDDEN);
            }
            if (address.isEmpty()) {
                return new ResponseEntity<>("You must to specify address", HttpStatus.FORBIDDEN);
            }
            if (mortgageValue <= 0) {
                return new ResponseEntity<>("You must to enter a value greather than 0", HttpStatus.FORBIDDEN);
            }
            if (image.isEmpty()) {
                return new ResponseEntity<>("You must to enter a valid url", HttpStatus.FORBIDDEN);
            }
            Optional<Property> property = propertyService.findByPropertyName(propertyName);
            Optional<Property> propertyNameUpdate = propertyService.findByPropertyName(newPropertyName);

            if (propertyNameUpdate.isPresent()) {
                return new ResponseEntity<>("New name assigned to property exists, so you can try with another name", HttpStatus.FORBIDDEN);
            }

            if (property.isPresent()) {
                Property updateProperty = property.get();
                if (available.equalsIgnoreCase("yes")) {
                    updateProperty.setAvailable(true);
                    Client assignedClient = updateProperty.getClient();
                    assignedClient.setProperties(null);
                    updateProperty.setClient(null);
                } else {
                    updateProperty.setAvailable(false);
                }

                if (property.get().isAvailable() == false) {
                    if (propertyService.citiesMinValueMortgage(city)) {
                        if (mortgageValue <= 2000000) {
                            return new ResponseEntity<>("Mortgage value must be greather than $2000000 in Cali and Bogotá", HttpStatus.FORBIDDEN);
                        }
                    }
                    if (propertyService.isAllowedLocation(city)) {
                        updateProperty.setPropertyName(newPropertyName);
                        updateProperty.setImage(image);

                        propertyService.saveProperty(updateProperty);
                        return new ResponseEntity<>("Fields value, city and address cannot be updated because the property is rented, other fields were updated successfully ", HttpStatus.ACCEPTED);
                    } else {
                        return new ResponseEntity<>("Property location doesn't allowed", HttpStatus.NOT_ACCEPTABLE);
                    }
                } else {
                    if (propertyService.citiesMinValueMortgage(city)) {
                        if (mortgageValue <= 2000000) {
                            return new ResponseEntity<>("Mortgage value must be greather than $2000000 in Cali and Bogotá", HttpStatus.FORBIDDEN);
                        }
                    }
                    if (propertyService.isAllowedLocation(city)) {
                        updateProperty.setPropertyName(newPropertyName);
                        updateProperty.setCity(city);
                        updateProperty.setAddress(address);
                        updateProperty.setMortgageValue(mortgageValue);
                        updateProperty.setImage(image);

                        propertyService.saveProperty(updateProperty);
                        return new ResponseEntity<>("Property updated successfully ", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Property location doesn't allowed", HttpStatus.NOT_ACCEPTABLE);
                    }
                }
            }
            return new ResponseEntity<>("Property not found", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error updating property: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while updating the property.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
