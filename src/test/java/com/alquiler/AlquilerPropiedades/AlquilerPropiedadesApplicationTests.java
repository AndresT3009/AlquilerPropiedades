package com.alquiler.AlquilerPropiedades;

import com.alquiler.AlquilerPropiedades.domain.models.Property;
import com.alquiler.AlquilerPropiedades.domain.models.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.domain.repository.PropertyRepository;
import com.alquiler.AlquilerPropiedades.infrastructure.service.ClientService;
import com.alquiler.AlquilerPropiedades.infrastructure.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class AlquilerPropiedadesApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Mock
	private PropertyService propertyService;

	@Mock
	private ClientService clientService;

	@Mock
	private PropertyRepository propertyRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testFindAllProperties() throws Exception {

		Property property1 = new Property("Property1", "City1", "Address1",true, 1000000, "image1", LocalDate.of(2024,12,8),false);
		propertyRepository.save(property1);
		Property property2 = new Property("Property2", "City2", "Address2",true, 2000000, "image2", LocalDate.now(),false);
		propertyRepository.save(property2);

		Mockito.when(propertyService.findAllProperties());

		mockMvc.perform(MockMvcRequestBuilders.get("/api/properties/all"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].propertyName").value("Property1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].propertyName").value("Property2"));
	}


}
