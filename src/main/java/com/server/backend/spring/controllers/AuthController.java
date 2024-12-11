package com.server.backend.spring.controllers;

import jakarta.validation.Valid;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.backend.spring.dtos.LoginUserDTO;
import com.server.backend.spring.dtos.RegisterUserDTO;
import com.server.backend.spring.models.User;
import com.server.backend.spring.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDTO request) {
  
    	User user = authService.register(request);
    	
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserDTO request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(token);
    }
}

