package com.alquiler.AlquilerPropiedades.domain.models.dto;

import com.alquiler.AlquilerPropiedades.domain.models.Property;
import lombok.Getter;
import java.time.LocalDate;

@Getter
public class PropertyDTO {
    private Long id;
    private String propertyName;
    private String city;
    private String address;
    private boolean available;
    private int mortgageValue;
    private String image;
    private LocalDate date;
    private boolean isDeleted;

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
        this.isDeleted = property.isDeleted();
    }

}
