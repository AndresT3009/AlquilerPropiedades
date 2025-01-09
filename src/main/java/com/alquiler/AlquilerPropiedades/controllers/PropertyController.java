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

    @PostMapping("/property")
    public ResponseEntity<?> insertProperty(
            @RequestParam String propertyName,
            @RequestParam String city,
            @RequestParam String address,
            @RequestParam Double mortgageValue,
            @RequestParam String image
    ) {
        if (propertyName.isEmpty()){
            return new ResponseEntity<>("You must to specify property Name", HttpStatus.FORBIDDEN);
        }
        if (city.isEmpty()) {
            return new ResponseEntity<>("You must to specify city", HttpStatus.FORBIDDEN);
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
        LocalDate date = LocalDate.now();

        Property newProperty = new Property(propertyName,city,address,available,mortgageValue,image,date);
        propertyService.saveProperty(newProperty);
        return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
    }


}
