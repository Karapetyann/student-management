package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/user/register").permitAll()
                .requestMatchers("/lesson").hasAnyAuthority(UserType.TEACHER.name(),UserType.STUDENT.name())
                .requestMatchers("/user").hasAnyAuthority(UserType.TEACHER.name(),UserType.STUDENT.name())
                .requestMatchers("lesson/add").hasAnyAuthority(UserType.TEACHER.name())
                .requestMatchers("lesson/delete/").hasAnyAuthority(UserType.TEACHER.name())
                .requestMatchers("user/delete/").hasAnyAuthority(UserType.TEACHER.name())
                .requestMatchers("user/update").hasAnyAuthority(UserType.TEACHER.name(),UserType.STUDENT.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutSuccessUrl("/");
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


}
