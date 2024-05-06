package org.sid.userservice.config;

import org.sid.userservice.entities.User;
import org.sid.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UsersDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByNom(username);
        return user.map(UsersDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
    }
}
