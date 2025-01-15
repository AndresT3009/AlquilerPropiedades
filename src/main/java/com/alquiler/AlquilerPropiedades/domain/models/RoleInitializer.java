package com.alquiler.AlquilerPropiedades.domain.models;

import com.alquiler.AlquilerPropiedades.domain.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

            roleRepository.save(new Role("ADMIN"));


            roleRepository.save(new Role("USER"));

    }
}
