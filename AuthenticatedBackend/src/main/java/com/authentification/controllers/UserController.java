package com.authentification.controllers;


import com.authentification.models.ApplicationUser;
import com.authentification.dtos.PasswordUpdateDTO;
import com.authentification.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")

public class UserController {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping("/")
    public String helloUserController() {
        return "User Access Level";
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(
            @RequestBody PasswordUpdateDTO passwordUpdate,
            Authentication authentication,
            HttpServletRequest request
    ) {
        String username = authentication.getName();
        String currentPassword = passwordUpdate.getCurrentPassword();
        String newPassword = passwordUpdate.getNewPassword();

        if (newPassword == null) {
            return ResponseEntity.badRequest().body("New password cannot be null");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(currentPassword, userDetails.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }
        ApplicationUser user = (ApplicationUser) userDetails;
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        // Retrieve the token
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Invalidate existing token
            String accessToken = authorizationHeader.substring(7);
         //   tokenBlackListRepo.save(new TokenBlacklist(null, accessToken));
        }
        return ResponseEntity.ok("Password updated successfully");
    }


}
