package com.chatExample.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class QuickPollUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with the username %s doesn't exist", username));
        }
        // Create a granted authority based on user's  role.
          // Can't pass null authorities to user. Hence initialize with an empty arraylist
      //  List<GrantedAuthority> authorities = new ArrayList<>();
        // Create a UserDetails object from the data
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}