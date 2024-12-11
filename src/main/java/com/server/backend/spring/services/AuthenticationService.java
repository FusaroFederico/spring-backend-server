package com.server.backend.spring.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.server.backend.spring.dtos.LoginUserDTO;
import com.server.backend.spring.dtos.RegisterUserDTO;
import com.server.backend.spring.models.User;
import com.server.backend.spring.repositories.UserRepository;
import com.server.backend.spring.security.DatabaseUserDetails;
import com.server.backend.spring.security.JwtService;

@Service
public class AuthenticationService {

    // Dipendenze iniettate tramite costruttore
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User register(RegisterUserDTO request) {
        // Creiamo un nuovo utente e codifichiamo la password
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Aggiungi ruoli se necessario
        return userRepository.save(user);
    }

    public String authenticate(LoginUserDTO request) {
        // Autentichiamo l'utente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Recuperiamo i dettagli dell'utente dal database
        UserDetails userDetails = userRepository.findByEmail(request.getEmail())
                .map(DatabaseUserDetails::new)
                .orElseThrow();

        // Generiamo il token JWT
        return jwtService.generateToken(userDetails);
    }
}

