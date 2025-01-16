package com.alquiler.AlquilerPropiedades.domain.security;

import com.alquiler.AlquilerPropiedades.domain.models.Client;
import com.alquiler.AlquilerPropiedades.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSecurityService  implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Autowired
    public UserSecurityService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + email));

        Set<GrantedAuthority> authorities = client.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(client.getEmail())
                .password(client.getPassword())
                .authorities(authorities)
                .build();
    }
}
