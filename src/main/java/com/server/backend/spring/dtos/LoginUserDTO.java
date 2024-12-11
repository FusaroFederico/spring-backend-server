package com.server.backend.spring.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginUserDTO {

	@NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    private String password;
    
    public String getEmail() {
    	return this.email;
    }
    public String getPassword() {
    	return this.password;
    }
}
