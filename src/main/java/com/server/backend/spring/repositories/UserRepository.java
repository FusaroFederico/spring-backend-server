package com.server.backend.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.backend.spring.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {

    //Cerco user per email
    Optional<User> findByEmail(String email);
}

