package com.authentification.controllers;


import com.authentification.dtos.RegistrationDTO;
import com.authentification.models.ApplicationUser;
import com.authentification.services.AuthenticationService;
import com.authentification.services.TokenService;
import com.authentification.dtos.LoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

@RestController
@RequestMapping("/auth")

public class AuthenticationController {

    private AuthenticationService authenticationService;
    private TokenService tokenService;

    public AuthenticationController(AuthenticationService authenticationService, TokenService tokenService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;

    }


    @PostMapping("/register-admin")
    public ApplicationUser registerAdmin(
            @RequestBody RegistrationDTO body
    ) {
        return authenticationService.registerAdmin(body.getUsername(), body.getPassword());
    }

    @PostMapping("/register")
    public ApplicationUser registerUser(
            @RequestBody RegistrationDTO body

    ) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/register-CE")
    public ApplicationUser registerCE(
            @RequestBody RegistrationDTO body

    ) {
        return authenticationService.registerCE(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(
        @RequestBody RegistrationDTO body
    ) throws AccountLockedException {
        System.out.println(body.getUsername());
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(
        @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
     //   tokenService.invalidateToken(jwt);
        return ResponseEntity.ok("Logged out successfully");

    }

}
