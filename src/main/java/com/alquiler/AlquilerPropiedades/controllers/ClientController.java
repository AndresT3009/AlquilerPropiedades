package com.alquiler.AlquilerPropiedades.controllers;

import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.models.Role;
import com.alquiler.AlquilerPropiedades.domain.models.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.domain.repository.RoleRepository;
import com.alquiler.AlquilerPropiedades.infrastructure.exceptions.ErrorResponse;
import com.alquiler.AlquilerPropiedades.infrastructure.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ClientService clientService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/clients/all")
    public ResponseEntity<?> findAll() {
        log.info("Retrieving all clients...");
        try {
            List<ClientDTO> clients = clientService.findAllClients();
            log.info("Successfully retrieved {} clients.", clients.size());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Message", "Request successful")
                    .body(clients);
        } catch (Exception e) {
            log.error("Error retrieving clients: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while retrieving clients.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/clients/search")
    public ResponseEntity<?> searchClient(@RequestParam long document) {
        log.info("Searching for client with document: {}", document);
        try {
            if (document <= 0) {
                log.warn("Invalid document value: {}", document);
                return new ResponseEntity<>("Invalid document value", HttpStatus.BAD_REQUEST);
            }
            Optional<Client> client = clientService.findByDocument(document);
            if (client.isEmpty()) {
                log.warn("Client not found with document: {}", document);
                return new ResponseEntity<>("Does not exist client whit this document", HttpStatus.NOT_FOUND);
            }
            log.info("Client found with document: {}", document);
            return ResponseEntity.status(HttpStatus.OK).header("Message", "Client found succesfully").body(client);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Error searching client: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while searching for the client.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/clients/save")
    public ResponseEntity<?> insertClient(
            @RequestParam String firstName,
            @RequestParam String secondName,
            @RequestParam String surName,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) Long document) {
        log.info("Attempting to save a new client with email: {}", email);
        try {
            Optional<Client> client = clientService.findByDocument(document);
            if (client.isPresent()) {
                log.warn("Client already exists with document: {}", document);
                return new ResponseEntity<>("Client already exist", HttpStatus.CONFLICT);
            }
            if (firstName.isEmpty()) {
                log.warn("First name was not inserted");
                return new ResponseEntity<>("You must to specify client First Name", HttpStatus.FORBIDDEN);
            }
            if (surName.isEmpty()) {
                log.warn("Sur name was not inserted");
                return new ResponseEntity<>("You must to specify client surname", HttpStatus.FORBIDDEN);
            }
            if (phoneNumber.isEmpty()) {
                log.warn("Phone number was not inserted");
                return new ResponseEntity<>("You must to specify client phone Number", HttpStatus.FORBIDDEN);
            }
            if (email.isEmpty() || password.isEmpty()) {
                log.warn("Email and password were not inserted");
                return new ResponseEntity<>("Email and password are required", HttpStatus.BAD_REQUEST);
            }
            if (document == null) {
                log.warn("Document was not inserted", phoneNumber);
                return new ResponseEntity<>("You must to specify client document", HttpStatus.FORBIDDEN);
            } else {
                String roleName = email.contains("@quind") ? "ADMIN" : "USER";
                log.info("Assigning role '{}' to new client '{}'", roleName, email);
                Role role = roleRepository.findByName(roleName);

                Client newClient = new Client(firstName, secondName, surName, phoneNumber, document);
                newClient.setEmail(email);
                newClient.setPassword(passwordEncoder.encode(password));
                newClient.setRoles(Collections.singleton(role));
                log.info("Roles assigned to client '{}': {}", email, newClient.getRoles());

                clientService.saveClient(newClient);
                log.info("Client successfully saved with email: {}", email);
                return new ResponseEntity<>("Client saved successfully", HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Validation failed. Check your input.", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error saving client: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse("An unexpected error occurred while saving the client.", e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
