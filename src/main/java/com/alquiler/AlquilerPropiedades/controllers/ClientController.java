package com.alquiler.AlquilerPropiedades.controllers;

import com.alquiler.AlquilerPropiedades.dto.ClientDTO;
import com.alquiler.AlquilerPropiedades.exceptions.ErrorResponse;
import com.alquiler.AlquilerPropiedades.jpa.entity.properties.Client;
import com.alquiler.AlquilerPropiedades.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping("/clients")
    public ResponseEntity<?> findAll() {
        try {
            List<ClientDTO> clients = clientService.findAllClients();
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

    @GetMapping("/search_client")
    public ResponseEntity<?> searchClient(@RequestParam long document) {
        try {
            if (document <= 0) {
                return new ResponseEntity<>("Invalid document value", HttpStatus.BAD_REQUEST);
            }
            Optional<Client> client = clientService.findByDocument(document);
            if (client.isEmpty()) {
                return new ResponseEntity<>("Does not exist client whit this document", HttpStatus.NOT_FOUND);
            }
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

    @PostMapping("save_client")
    public ResponseEntity<?> insertClient(
            @RequestParam String firstName,
            @RequestParam String secondName,
            @RequestParam String surName,
            @RequestParam String phoneNumber,
            @RequestParam(required = false) Long document) {
        try {
            Optional<Client> client = clientService.findByDocument(document);
            if (client.isPresent()) {
                return new ResponseEntity<>("Client already exist", HttpStatus.CONFLICT);
            }
            if (firstName.isEmpty()) {
                return new ResponseEntity<>("You must to specify client First Name", HttpStatus.FORBIDDEN);
            }
            if (surName.isEmpty()) {
                return new ResponseEntity<>("You must to specify client surname", HttpStatus.FORBIDDEN);
            }
            if (phoneNumber.isEmpty()) {
                return new ResponseEntity<>("You must to specify client phone Number", HttpStatus.FORBIDDEN);
            }
            if (document == null) {
                return new ResponseEntity<>("You must to specify client document", HttpStatus.FORBIDDEN);
            } else {
                Client newClient = new Client(firstName, secondName, surName, phoneNumber, document);
                clientService.saveClient(newClient);
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
