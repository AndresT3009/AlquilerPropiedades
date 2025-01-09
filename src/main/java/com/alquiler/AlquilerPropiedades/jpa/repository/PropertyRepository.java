package com.alquiler.AlquilerPropiedades.jpa.repository;

import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository <Property, Long> {



}
