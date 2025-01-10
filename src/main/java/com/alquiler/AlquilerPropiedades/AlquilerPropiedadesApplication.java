package com.alquiler.AlquilerPropiedades;

import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Property;
import com.alquiler.AlquilerPropiedades.jpa.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
@Slf4j
public class AlquilerPropiedadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlquilerPropiedadesApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(

			PropertyRepository propertyRepository

	){
		return args ->{

			log.info("Application started");

			Property property = new Property("Apartamento La Vega 203","Medellin","carrera 34 # 75 - 20",true, 2000000,"www.images.com" , LocalDate.of(2024,12,8),false);
			propertyRepository.save(property);

			Property property2 = new Property("Apartamento La Vega 204","Medellin","carrera 34 # 75 - 20",true, 1500000,"www.images.com" , LocalDate.of(2024,12,30),true);
			propertyRepository.save(property2);
		};

	}
}
