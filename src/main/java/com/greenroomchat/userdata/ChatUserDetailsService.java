package com.greenroomchat.userdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ChatUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    Pattern usernameRegex = Pattern.compile("[\\s!@#`~$%\\\\^?&*()\\-\\[,.<>/;':\"+=|\\]]");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username.toUpperCase());
        if (user == null) {
            throw new UsernameNotFoundException(String.format(username+" not found"));
        }
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public void save(User user) {
        Matcher matcher = usernameRegex.matcher(user.getUsername());
        if (!matcher.find()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUsername(user.getUsername().toUpperCase());
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Username contains illegal characters");
        }
    }
}