package com.authentification.controllers;

import com.authentification.models.ApplicationUser;
import com.authentification.repositories.RoleRepo;
import com.authentification.repositories.UserRepo;
import com.authentification.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/CE")

public class CEController {
    private final UserRepo userRepo;
    private final UserService userService;
    private final RoleRepo roleRepo;

    @Autowired
    public CEController(UserRepo userRepo, UserService userService, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @GetMapping("/")
    public String helloCEController() {
        return "CE Level Access";
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ApplicationUser>> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ApplicationUser> users = userService.findAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/new-user")
    public ResponseEntity<ApplicationUser> addNewUser(
            @RequestBody ApplicationUser user
    ) {
        userService.addNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PostMapping("/add-role") //not working
    public ResponseEntity<?> addRoleToUser(
            @RequestParam String username,
            @RequestParam String roleName
    ) {
        userService.addRoleToUser(username, roleName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
