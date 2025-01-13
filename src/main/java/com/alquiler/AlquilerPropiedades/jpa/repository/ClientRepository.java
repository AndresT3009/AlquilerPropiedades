package com.alquiler.AlquilerPropiedades.jpa.repository;

import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository <Client, Long> {

    @Query("SELECT p FROM Client p WHERE p.document = ?1")
    Optional<Client> findByDocument (Long document);

}
