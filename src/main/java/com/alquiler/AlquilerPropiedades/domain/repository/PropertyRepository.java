package com.alquiler.AlquilerPropiedades.domain.repository;

import com.alquiler.AlquilerPropiedades.domain.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository <Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.mortgageValue BETWEEN ?1 AND ?2 AND p.isDeleted = false AND p.available = true")
    List<Property> findByMortgageValue(int min, int max);

    @Query("SELECT p FROM Property p WHERE p.propertyName = ?1")
    Optional<Property> findByPropertyName(String propertyName);

    @Query ("SELECT p FROM Property p WHERE p.id = ?1")
    Optional<Property> findByPropertyId(Long id);

    @Modifying
    @Query("UPDATE Property p SET p.isDeleted = true WHERE p.propertyName = ?1")
    void modifyDeletedValue (String propertyName);
}
