package com.authentification.services;

import com.authentification.models.ApplicationUser;
import com.authentification.models.Role;
import com.authentification.repositories.RoleRepo;
import com.authentification.repositories.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder encoder;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public UserService(PasswordEncoder encoder, UserRepo userRepo, RoleRepo roleRepo) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    public Page<ApplicationUser> findAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    public void addNewUser(ApplicationUser user) {
        String newPass = encoder.encode(user.getPassword());
        user.setPassword(newPass);
        userRepo.save(user);
    }

    public void addNewRole(Role role) {
        // Sauvegarder le nouveau rôle
        Role savedRole = roleRepo.save(role);

        // Maintenant, vous pouvez ajouter ce rôle à tous les utilisateurs existants
        List<ApplicationUser> users = userRepo.findAll();
        for (ApplicationUser user : users) {
            Set<Role> authorities = convertAuthorities(user.getAuthorities());
            authorities.add(savedRole);
            user.setAuthorities(authorities);
            userRepo.save(user);
        }
    }

    public void addRoleToUser(String username, String roleName) {
        Optional<ApplicationUser> userOptional = userRepo.findByUsername(username);
        Optional<Role> roleOptional = roleRepo.findByAuthority(roleName);

        if (userOptional.isPresent() && roleOptional.isPresent()) {
            ApplicationUser user = userOptional.get();
            Role role = roleOptional.get();
            Set<Role> authorities = convertAuthorities(user.getAuthorities());
            authorities.add(role);
            user.setAuthorities(authorities);
            userRepo.save(user);
        }
    }

    private Set<Role> convertAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<Role> roles = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof Role) {
                roles.add((Role) authority);
            } else {
                // Handle other types of GrantedAuthority if necessary
            }
        }
        return roles;
    }
}
