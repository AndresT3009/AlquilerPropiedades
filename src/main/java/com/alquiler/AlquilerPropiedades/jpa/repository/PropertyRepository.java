package com.alquiler.AlquilerPropiedades.jpa.repository;

import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository <Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.mortgageValue BETWEEN ?1 AND ?2 AND p.isDeleted = false AND p.available = true")
    List<Property> findByMortgageValue(int min, int max);

    @Query("SELECT p FROM Property p WHERE p.propertyName = ?1 AND p.isDeleted = false AND p.available = true")
    List<Property> findByPropertyName(String propertyName);

}
