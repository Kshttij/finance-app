package com.krish.finance.finance_server.controller;

import com.krish.finance.finance_server.dto.LoginRequest;
import com.krish.finance.finance_server.dto.LoginResponse;
import com.krish.finance.finance_server.dto.SignupRequest;
import com.krish.finance.finance_server.model.User;
import com.krish.finance.finance_server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
public ResponseEntity<LoginResponse> signup(@RequestBody SignupRequest request) {
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());

    User createdUser = userService.createUser(user);

    LoginResponse response = new LoginResponse(
        createdUser.getId(),
        createdUser.getName(),
        createdUser.getEmail(),
        "Signup successful"
    );
    return new ResponseEntity<>(response, HttpStatus.CREATED);
}

@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    User existingUser = userService.getUserByEmail(request.getEmail());

    if (existingUser == null) {
        return new ResponseEntity<>(
            new LoginResponse(null, null, null, "User not found"),
            HttpStatus.NOT_FOUND
        );
    }

    boolean passwordMatches = userService.checkPassword(request.getPassword(), existingUser.getPassword());

    if (!passwordMatches) {
        return new ResponseEntity<>(
            new LoginResponse(null, null, null, "Invalid password"),
            HttpStatus.UNAUTHORIZED
        );
    }

    return new ResponseEntity<>(
        new LoginResponse(
            existingUser.getId(),
            existingUser.getName(),
            existingUser.getEmail(),
            "Login successful"
        ),
        HttpStatus.OK
    );
}

}
