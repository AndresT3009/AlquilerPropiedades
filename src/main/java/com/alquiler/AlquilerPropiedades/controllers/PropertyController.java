package com.alquiler.AlquilerPropiedades.controllers;

import com.alquiler.AlquilerPropiedades.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;
import com.alquiler.AlquilerPropiedades.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @GetMapping("/properties")
    public List<PropertyDTO> findAll() {
        return propertyService.findAllProperties();
    }

    @GetMapping("/search_value")
    public  ResponseEntity<?> searchProperty(
            @RequestParam int minValue,
            @RequestParam int maxValue
    ) {
        if (minValue <= 0 || maxValue <= 0 || minValue > maxValue) {
            return new ResponseEntity<>("Invalid range values", HttpStatus.BAD_REQUEST);
        }
        List<PropertyDTO> properties = propertyService.findByValue(minValue, maxValue);
        if (properties.isEmpty()) {
            return new ResponseEntity<>("No properties found in the given range", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }


    @PostMapping("/property")
    public ResponseEntity<?> insertProperty(
            @RequestParam String propertyName,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam int mortgageValue,
            @RequestParam String image
    ) {
        if (propertyName.isEmpty()){
            return new ResponseEntity<>("You must to specify property Name", HttpStatus.FORBIDDEN);
        }
        if (city.isEmpty()) {
            return new ResponseEntity<>("You must to specify city", HttpStatus.FORBIDDEN);
        } else if (propertyService.citiesMinValueMortgage(city)) {
            if(mortgageValue <=2000000){
                return new ResponseEntity<>("Mortgage value must be greather than $2000000 in Cali and Bogotá",HttpStatus.FORBIDDEN);
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

        List<PropertyDTO> propertiesList = propertyService.findByPropertyName(propertyName);
        if (!propertiesList.isEmpty()) {
            return new ResponseEntity<>("Property name already exists", HttpStatus.CONFLICT);
        }else{
            if(propertyService.isAllowedLocation(city)){

                Property newProperty = new Property(propertyName,city,address,available,mortgageValue,image,date,isDeleted);
                propertyService.saveProperty(newProperty);
                return new ResponseEntity<>(newProperty, HttpStatus.CREATED);

            }else{
                return new ResponseEntity<>("Property location doesn't allowed", HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


}
