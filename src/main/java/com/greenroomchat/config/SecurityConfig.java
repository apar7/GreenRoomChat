package com.greenroomchat.config;


import com.greenroomchat.userdata.ChatUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private HashSet<String> userList;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private ChatUserDetailsService userDetailsService;

    @Autowired
    public void setUserList(HashSet<String> userList) {
        this.userList = userList;
    }

    @Autowired
    public void setUserDetailsService(ChatUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(userDetailsService.getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/").authenticated()
                .and().formLogin()
                .loginPage("/login")
                .permitAll()
                .failureUrl("/login?incorrect")
                .successHandler(this::handleSuccess)
                .and().logout().permitAll()
                .and().httpBasic()
                .and().csrf().disable();
    }

    private void handleSuccess(HttpServletRequest request,
                               HttpServletResponse response,
                               Authentication authentication) throws IOException {
        String username = authentication.getName();
        if (userList.contains(username)) {
            logger.info(username + " is currently in use");
            response.sendRedirect("/login?userinuse");
        } else {
            response.sendRedirect("/");
        }
    }
}
