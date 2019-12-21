package com.example.demo.security;

import org.springframework.security.core.userdetails.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

/**
 * When a user tries to authenticate, this method receives the username,
 * searches the database for a record containing it, if found returns an instance of User.
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        com.example.demo.model.persistence.User user = userRepository.findByUsername(userName);
        if (user==null){
            throw new UsernameNotFoundException(userName);
        }
        return new User(user.getUsername(), user.getPassowrd(), Collections.emptyList());
    }
}
