package com.financeapp.backend.dto;

import jakarta.validation.constraints.NotBlank;

// A DTO (Data Transfer Object) for handling login requests.
// We use this so we don't send the entire 'User' object over the wire.
// It also allows us to add validation.
public class AuthRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    // Getters and Setters are needed for Jackson (the JSON library)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}