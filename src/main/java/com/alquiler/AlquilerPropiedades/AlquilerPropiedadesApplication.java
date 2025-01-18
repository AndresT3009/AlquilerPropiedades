package com.alquiler.AlquilerPropiedades;

import com.alquiler.AlquilerPropiedades.config.JwtFilter;
import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.models.Property;
import com.alquiler.AlquilerPropiedades.domain.models.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.domain.repository.ClientRepository;
import com.alquiler.AlquilerPropiedades.domain.repository.PropertyRepository;
import com.alquiler.AlquilerPropiedades.domain.security.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.alquiler.AlquilerPropiedades")
public class AlquilerPropiedadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlquilerPropiedadesApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			PropertyRepository propertyRepository,
			ClientRepository clientRepository
			//JwtFilter jwtFilter,
			//UserSecurityService userSecurityService
	){
		return args ->{
			log.info("Application started");

			Property property = new Property("Apartamento La Vega 203","Medellin","carrera 34 # 75 - 20",true, 2000000,"www.images.com" , LocalDate.of(2024,12,8),false);
			propertyRepository.save(property);

			Property property2 = new Property("Apartamento La Vega 204","Medellin","carrera 34 # 75 - 20",true, 1500000,"www.images.com" , LocalDate.of(2024,12,01),true);
			propertyRepository.save(property2);

			Client client = new Client("Darwin", "Andrés", "Tangarife", "3015781171",1017137654L);
			clientRepository.save(client);
		};
	}
}
