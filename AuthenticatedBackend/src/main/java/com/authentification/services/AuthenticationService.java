package com.authentification.services;


import com.authentification.models.ApplicationUser;
import com.authentification.dtos.LoginResponseDTO;
import com.authentification.models.Role;
import com.authentification.repositories.RoleRepo;
import com.authentification.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;


    public ApplicationUser registerAdmin(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepo.findByAuthority("CP").orElseThrow(() -> new RuntimeException("CP Role not found"));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        return userRepo.save(new ApplicationUser(username, encodedPassword, authorities));
    }

    public ApplicationUser registerUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepo.findByAuthority("TEST").orElseThrow(() -> new RuntimeException("Test Role not found"));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        return userRepo.save(new ApplicationUser(username, encodedPassword, authorities));
    }


    public ApplicationUser registerCE(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepo.findByAuthority("CE").orElseThrow(() -> new RuntimeException("CE Role not found"));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        return userRepo.save(new ApplicationUser(username, encodedPassword, authorities));
    }

    public LoginResponseDTO loginUser(String username, String password) throws AccountLockedException {
        try {
            ApplicationUser user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

            if (!user.isAccountNonLocked()) {
                throw new AccountLockedException("Account is locked");
            }

            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // Reset consecutive wrong attempts if authentication succeeds
            user.setConsecutiveWrongAttempts(0);
            userRepo.save(user);
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(user, token);
        } catch (BadCredentialsException e) {
            try {
                ApplicationUser user = userRepo.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
                // Increment consecutive wrong attempts and check if the account should be locked
                int newConsecutiveWrongAttempts = user.getConsecutiveWrongAttempts() + 1;
                user.setConsecutiveWrongAttempts(newConsecutiveWrongAttempts);
                if (newConsecutiveWrongAttempts >= 3) {
                    user.setLocked(true);
                    // Implement additional logic such as sending notifications or blocking access
                    System.out.println("account locked exception");
                    throw new AccountLockedException("Account is locked");
                } else {
                    userRepo.save(user);
                    throw e; // Re-throw the BadCredentialsException to indicate invalid credentials
                }
            } catch (AccountLockedException accountLockedException) {
                // Handle AccountLockedException specifically
                System.out.println("account locked exception");
                throw accountLockedException;
            }
        }
    }



}
