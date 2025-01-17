package com.alquiler.AlquilerPropiedades.domain.repository;

import com.alquiler.AlquilerPropiedades.domain.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT p FROM Client p WHERE p.document = ?1")
    Optional<Client> findByDocument(Long document);

    Optional<Client> findByEmail(String email);

}
