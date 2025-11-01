package com.financeapp.backend.dto;

import jakarta.validation.constraints.NotBlank;

// A DTO for handling registration requests.
// This is more secure than using the 'User' model directly,
// as it prevents a user from trying to set fields like 'id'.
public class UserRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    // Getters and Setters
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