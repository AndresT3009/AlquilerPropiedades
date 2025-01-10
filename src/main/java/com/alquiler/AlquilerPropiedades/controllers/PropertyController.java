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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class PropertyController {

    @Autowired
    PropertyService propertyService;

    @GetMapping("/properties")
    public ResponseEntity<List<PropertyDTO>> findAll() {
        List<PropertyDTO> properties = propertyService.findAllProperties();
        return ResponseEntity.status(HttpStatus.OK)
                .header("Message", "Request successful")
                .body(properties);
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
        return ResponseEntity.status(HttpStatus.OK).header("Message","Request succesfull").body(properties);
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

        Optional<PropertyDTO> property = propertyService.findByPropertyName(propertyName);
        if (property.isPresent()) {
            return new ResponseEntity<>("Property name already exist and its avalability is "+ property.get().isAvailable(), HttpStatus.CONFLICT);
        }else{
            if(propertyService.isAllowedLocation(city)){

                Property newProperty = new Property(propertyName,city,address,available,mortgageValue,image,date,isDeleted);
                propertyService.saveProperty(newProperty);
                return ResponseEntity.status(HttpStatus.CREATED).header("Message","Property created succesfully").body(newProperty);
            }else{
                return new ResponseEntity<>("Property location doesn't allowed", HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @PatchMapping("/delete_property")
    public  ResponseEntity<?> searchProperty(
            @RequestParam String propertyName
    ){
        if (propertyName.isEmpty()){
            return new ResponseEntity<>("You must to specify property Name", HttpStatus.FORBIDDEN);
        }
        Optional<PropertyDTO> property = propertyService.findByPropertyName(propertyName);
        if (property.isPresent()){
            if(property.get().getDate().plusDays(30).isBefore(LocalDate.now())){
                propertyService.modifyDeletedValue(propertyName);
                return ResponseEntity.status(HttpStatus.OK).header("Message","Property deleted succesfully").body("Property deleted succesfully");
            }else{
                return new ResponseEntity<>("Property cannot be deleted because the property creation date is less than one month ", HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
    }



}
