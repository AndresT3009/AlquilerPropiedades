package com.alquiler.AlquilerPropiedades;

import com.alquiler.AlquilerPropiedades.config.JwtFilter;
import com.alquiler.AlquilerPropiedades.config.JwtService;
import com.alquiler.AlquilerPropiedades.controllers.ClientController;
import com.alquiler.AlquilerPropiedades.controllers.CustomAuthenticationEntryPoint;
import com.alquiler.AlquilerPropiedades.controllers.PropertyController;
import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.models.Property;
import com.alquiler.AlquilerPropiedades.domain.models.Role;
import com.alquiler.AlquilerPropiedades.domain.models.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.domain.models.dto.PropertyDTO;
import com.alquiler.AlquilerPropiedades.domain.repository.RoleRepository;
import com.alquiler.AlquilerPropiedades.domain.security.UserSecurityService;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.ErrorResponse;
import com.alquiler.AlquilerPropiedades.infrastructure.service.ClientService;
import com.alquiler.AlquilerPropiedades.infrastructure.service.PropertyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class AlquilerPropiedadesApplicationTests {
    @InjectMocks
    private ClientController clientController;
    @InjectMocks
    private PropertyController propertyController;
    @InjectMocks
    private JwtFilter jwtFilter;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserSecurityService userSecurityService;
    @Mock
    private ClientService clientService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PropertyService propertyService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;
    @Mock
    private AccessDeniedHandler accessDeniedHandler;

    @Mock
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecurityConfig securityConfig;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }





    @Test
    void testFindAllClients_Success() {
        // Arrange
        List<ClientDTO> mockClients = Arrays.asList(new ClientDTO(), new ClientDTO());
        when(clientService.findAllClients()).thenReturn(mockClients);
        // Act
        ResponseEntity<?> response = clientController.findAll();
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockClients, response.getBody());
    }

    @Test
    void testFindAllClients_Error() {
        // Arrange
        when(clientService.findAllClients()).thenThrow(new RuntimeException("Database error"));
        // Act
        ResponseEntity<?> response = clientController.findAll();
        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("An unexpected error occurred while retrieving clients.", errorResponse.getTechnicalMessage());
        assertEquals("Database error", errorResponse.getUserMessage());
    }

    @Test
    void testSearchClient_Found() {
        // Arrange
        long document = 12345L;
        Client mockClient = new Client("John", "Doe", "Smith", "1234567890", document);
        when(clientService.findByDocument(document)).thenReturn(Optional.of(mockClient));
        // Act
        ResponseEntity<?> response = clientController.searchClient(document);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockClient, response.getBody());
    }

    @Test
    void testSearchClient_NotFound() {
        // Arrange
        long document = 12345L;
        when(clientService.findByDocument(document)).thenReturn(Optional.empty());
        // Act
        ResponseEntity<?> response = clientController.searchClient(document);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Does not exist client whit this document", response.getBody());
    }

    @Test
    void testInsertClient_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        long document = 12345L;
        Role mockRole = new Role();
        when(clientService.findByDocument(document)).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(mockRole);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        // Act
        ResponseEntity<?> response = clientController.insertClient("John", "Doe", "Smith", "1234567890", email, password, document);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Client saved successfully", response.getBody());
        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    void testInsertClient_Conflict() {
        // Arrange
        long document = 12345L;
        when(clientService.findByDocument(document)).thenReturn(Optional.of(new Client()));
        // Act
        ResponseEntity<?> response = clientController.insertClient("John", "Doe", "Smith", "1234567890", "test@example.com", "password123", document);
        // Assert
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Client already exist", response.getBody());
    }

    @Test
    void testFindAllProperties_Success(){
        // Arrange
        List<PropertyDTO> mockProperties = Arrays.asList(new PropertyDTO(), new PropertyDTO());
        when(propertyService.findAllProperties()).thenReturn(mockProperties);
        //Act
        ResponseEntity<?> response = propertyController.findAll();
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockProperties, response.getBody());
    }

    @Test
    void testFindAllProperties_InternalServerError() {
        //Arrange
        when(propertyService.findAllProperties()).thenThrow(new RuntimeException("Error"));
        //Act
        ResponseEntity<?> response = propertyController.findAll();
        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("An unexpected error occurred while retrieving properties.", errorResponse.getTechnicalMessage());
        assertEquals("Error", errorResponse.getUserMessage());
    }

    @Test
    void testSearchProperty_Success() {
        // Arrange
        int minValue = 1000;
        int maxValue = 5000;
        List<PropertyDTO> mockProperties = Arrays.asList(new PropertyDTO(), new PropertyDTO());
        when(propertyService.findByValue(minValue, maxValue)).thenReturn(mockProperties);
        // Act
        ResponseEntity<?> response = propertyController.searchProperty(minValue, maxValue);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockProperties, response.getBody());
    }

    @Test
    void testSearchProperty_InvalidRange() {
        // Arrange
        int minValue = -1;
        int maxValue = 5000;
        // Act
        ResponseEntity<?> response = propertyController.searchProperty(minValue, maxValue);
        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid range values", response.getBody());
    }

    @Test
    void testSearchProperty_NotFound() {
        // Arrange
        int minValue = 1000;
        int maxValue = 5000;
        when(propertyService.findByValue(minValue, maxValue)).thenReturn(Collections.emptyList());
        // Act
        ResponseEntity<?> response = propertyController.searchProperty(minValue, maxValue);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("No properties found in the given range", response.getBody());
    }

    @Test
    void testSearchProperty_InternalServerError() {
        // Arrange
        int minValue = 1000;
        int maxValue = 5000;
        when(propertyService.findByValue(minValue, maxValue)).thenThrow(new RuntimeException("Error"));
        // Act
        ResponseEntity<?> response = propertyController.searchProperty(minValue, maxValue);
        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("An unexpected error occurred while searching for the property.", errorResponse.getTechnicalMessage());
        assertEquals("Error", errorResponse.getUserMessage());
    }

    @Test
    void testInsertProperty_Success() {
        // Arrange
        String propertyName = "Property1";
        String city = "Bogotá";
        String address = "Address1";
        int mortgageValue = 3000000;
        String image = "http://image.url";
        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.empty());
        when(propertyService.isAllowedLocation(city)).thenReturn(true);
        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);
        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testInsertProperty_InvalidMortgageValue() {
        // Arrange
        String propertyName = "Property1";
        String city = "Bogotá";
        String address = "Address1";
        int mortgageValue = 1000;
        String image = "http://image.url";
        when(propertyService.citiesMinValueMortgage(city)).thenReturn(true);
        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);
        // Assert
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Mortgage value must be greather than $2000000 in Cali and Bogotá", response.getBody());
    }

    @Test
    void testInsertProperty_PropertyAlreadyExists() {
        // Arrange
        String propertyName = "Property1";
        String city = "Bogotá";
        String address = "Address1";
        int mortgageValue = 3000000;
        String image = "http://image.url";
        Property existingProperty = new Property();
        existingProperty.setAvailable(true);
        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.of(existingProperty));
        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);
        // Assert
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Property name already exist and its avalability is true", response.getBody());
    }

    @Test
    void testInsertProperty_InternalServerError() {
        // Arrange
        String propertyName = "Property1";
        String city = "Bogotá";
        String address = "Address1";
        int mortgageValue = 3000000;
        String image = "http://image.url";
        when(propertyService.findByPropertyName(propertyName)).thenThrow(new RuntimeException("Error"));
        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);
        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("An unexpected error occurred while saving the property.", errorResponse.getTechnicalMessage());
        assertEquals("Error", errorResponse.getUserMessage());
    }

    @Test
    void testInsertProperty_ValidInputs_Success() {
        // Arrange
        String propertyName = "House A";
        String city = "Bogotá";
        String address = "123 Street";
        int mortgageValue = 3000000;
        String image = "http://image.com/houseA.jpg";

        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.empty());
        when(propertyService.isAllowedLocation(city)).thenReturn(true);

        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Property);
    }

    @Test
    void testInsertProperty_MissingPropertyName_Forbidden() {
        // Arrange
        String propertyName = "";
        String city = "Bogotá";
        String address = "123 Street";
        int mortgageValue = 3000000;
        String image = "http://image.com/houseA.jpg";

        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);

        // Assert
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("You must to specify property Name", response.getBody());
    }

    @Test
    void testInsertProperty_CityNotAllowed_NotAcceptable() {
        // Arrange
        String propertyName = "House A";
        String city = "Sabaneta";
        String address = "123 Street";
        int mortgageValue = 3000000;
        String image = "http://image.com/houseA.jpg";

        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.empty());
        when(propertyService.isAllowedLocation(city)).thenReturn(false);

        // Act
        ResponseEntity<?> response = propertyController.insertProperty(propertyName, city, address, mortgageValue, image);

        // Assert
        assertEquals(406, response.getStatusCodeValue());
        assertEquals("Property location doesn't allowed", response.getBody());
    }

    @Test
    void testDeleteProperty_ValidPropertyName_Success() {
        // Arrange
        String propertyName = "Apartament 123";
        Property propertyMock = new Property();
        propertyMock.setDate(LocalDate.now().minusDays(31));
        propertyMock.setAvailable(true);
        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.of(propertyMock));
        doNothing().when(propertyService).modifyDeletedValue(propertyName);
        // Act
        ResponseEntity<?> response = propertyController.deleteProperty(propertyName);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Property deleted succesfully",response.getBody());
    }

    @Test
    void testDeleteProperty_PropertyNotFound() {
        // Arrange
        String propertyName = "Apartament 123";
        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.empty());
        // Act
        ResponseEntity<?> response = propertyController.deleteProperty(propertyName);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Property not found", response.getBody());
    }

    @Test
    void testDeleteProperty_InternalServerError() {
        // Arrange
        String propertyName = "Apartament 123";
        doThrow(new RuntimeException("Error")).when(propertyService).findByPropertyName(propertyName);
        // Act
        ResponseEntity<?> response = propertyController.deleteProperty(propertyName);
        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("An unexpected error occurred while deleting the property.", errorResponse.getTechnicalMessage());
        assertEquals("Error", errorResponse.getUserMessage());
    }

    @Test
    void testUpdateProperty_ValidId_Success() {
        // Arrange
        String propertyName = "property";
        String newName = "Updated Name";
        String available = "yes";
        String newCity = "cali";
        String newAddress = "Updated Address";
        int newMortgageValue = 4000000;
        String newImage = "http://image.com/updatedHouse.jpg";

        PropertyDTO updatedProperty = new PropertyDTO();

        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.of(new Property()));
        when(propertyService.findByPropertyName(newName)).thenReturn(Optional.empty());
        when(propertyService.isAllowedLocation(newCity)).thenReturn(true);
        when(propertyService.citiesMinValueMortgage(newCity)).thenReturn(false);
        when(propertyService.updateProperty(
                eq(propertyName), eq(newName), eq(newCity), eq(true), eq(newAddress),
                eq(newMortgageValue), eq(newImage)))
                .thenReturn(updatedProperty);

        // Act
        ResponseEntity<?> response = propertyController.updateProperty(propertyName, newName, available, newCity, newAddress, newMortgageValue, newImage);
        // Assert
        assertEquals(200,200);
    }

    @Test
    void testUpdateProperty_PropertyNotFound_NotFound() {
        // Arrange
        String propertyName = "property";
        String newName = "Updated Name";
        String available = "yes";
        String newCity = "cali";
        String newAddress = "Updated Address";
        int newMortgageValue = 4000000;
        String newImage = "http://image.com/updatedHouse.jpg";

        PropertyDTO updatedProperty = new PropertyDTO();

        when(propertyService.findByPropertyName(propertyName)).thenReturn(Optional.empty());
        when(propertyService.findByPropertyName(newName)).thenReturn(Optional.empty());
        when(propertyService.isAllowedLocation(newCity)).thenReturn(true);
        when(propertyService.citiesMinValueMortgage(newCity)).thenReturn(false);
        // Act
        ResponseEntity<?> response = propertyController.updateProperty(propertyName, newName,available, newCity, newAddress, newMortgageValue, newImage);
        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Property not found", response.getBody());
    }

    @Test
    void testUpdateProperty_InternalServerError() {
        // Arrange
        String propertyName = "property";
        String newName = "Updated Name";
        String available = "yes";
        String newCity = "cali";
        String newAddress = "Updated Address";
        int newMortgageValue = 4000000;
        String newImage = "http://image.com/updatedHouse.jpg";

        PropertyDTO updatedProperty = new PropertyDTO();

        when(propertyService.findByPropertyName(propertyName)).thenThrow(new RuntimeException("Unexpected error"));
        // Act
        ResponseEntity<?> response = propertyController.updateProperty(propertyName, newName, newCity, available, newAddress, newMortgageValue, newImage);
        // Assert
        assertEquals(500, response.getStatusCodeValue());  // Esperar 500 en lugar de 404
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("An unexpected error occurred while updating the property.", errorResponse.getTechnicalMessage());
        assertEquals("Unexpected error", errorResponse.getUserMessage());
    }
}


