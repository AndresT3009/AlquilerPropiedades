package com.alquiler.AlquilerPropiedades;

import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.models.Property;
import com.alquiler.AlquilerPropiedades.domain.repository.ClientRepository;
import com.alquiler.AlquilerPropiedades.domain.repository.PropertyRepository;
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

			PropertyRepository propertyRepository,
			ClientRepository clientRepository

	){
		return args ->{

			log.info("Application started");

			Property property = new Property("Apartamento La Vega 203","Medellin","carrera 34 # 75 - 20",true, 2000000,"www.images.com" , LocalDate.of(2024,12,8),false);
			propertyRepository.save(property);

			Property property2 = new Property("Apartamento La Vega 204","Medellin","carrera 34 # 75 - 20",true, 1500000,"www.images.com" , LocalDate.of(2024,12,30),true);
			propertyRepository.save(property2);

			Client client = new Client("Darwin", "Andrés", "Tangarife", "3015781171",1017137654L);
			clientRepository.save(client);
		};



	}
}
