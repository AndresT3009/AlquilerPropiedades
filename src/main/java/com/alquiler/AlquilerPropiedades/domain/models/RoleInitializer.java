package com.alquiler.AlquilerPropiedades.domain.models;

import com.alquiler.AlquilerPropiedades.domain.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Saving new role ADMIN");
        roleRepository.save(new Role("ADMIN"));
        log.info("Saving new role USER");
        roleRepository.save(new Role("USER"));

    }
}
