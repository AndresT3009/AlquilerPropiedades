package com.alquiler.AlquilerPropiedades.domain.repository;

import com.alquiler.AlquilerPropiedades.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
