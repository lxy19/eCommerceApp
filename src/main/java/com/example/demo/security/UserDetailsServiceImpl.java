package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * When a user tries to authenticate, this method receives the username,
 * searches the database for a record containing it, if found returns an instance of User.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User applicationUser = userRepository.findByUsername(userName);
        if (applicationUser==null){
            throw new UsernameNotFoundException(userName);
        }
        return new org.springframework.security.core.userdetails.User(applicationUser.getUsername(), applicationUser.getPassowrd(), Collections.emptyList());
    }
}
