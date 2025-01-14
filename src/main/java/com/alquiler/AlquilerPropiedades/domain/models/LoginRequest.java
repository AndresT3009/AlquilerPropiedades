package com.alquiler.AlquilerPropiedades.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
    private Set<String> roles;

}
