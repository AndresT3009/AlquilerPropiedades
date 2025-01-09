package com.alquiler.AlquilerPropiedades.dto;


import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PropertyDTO {

    private Long id;

    private String propertyName;

    private String city;

    private String address;

    private boolean available;

    private Double mortgageValue;

    private String image;

    private LocalDate date;

    public PropertyDTO() {
    }

    public PropertyDTO(Property property) {
        this.id = property.getId();
        this.propertyName = property.getPropertyName();
        this.city = property.getCity();
        this.address = property.getAddress();
        this.available = property.isAvailable();
        this.mortgageValue = property.getMortgageValue();
        this.image = property.getImage();
        this.date = property.getDate();
    }
}
