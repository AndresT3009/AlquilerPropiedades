package com.alquiler.AlquilerPropiedades.domain.repository;

import com.alquiler.AlquilerPropiedades.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
