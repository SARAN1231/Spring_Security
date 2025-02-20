package com.saran.SecurityDemo.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.saran.SecurityDemo.Models.Permission.*;
import static com.saran.SecurityDemo.Models.Role.ADMIN;
import static com.saran.SecurityDemo.Models.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;

@Configuration // saying it is Config File
@EnableWebSecurity // Don't go with default spring security config . Go with my custom config
@EnableMethodSecurity // for using preauthorize annotation in the controllers
public class SecurityConfig {


    private final UserDetailsService userDetailsService;

    @Autowired
    private  JWTFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService ) {
        this.userDetailsService = userDetailsService;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer->customizer.disable()); // in default spring security create csrf token in each req to prevent csrf attacks. In this case csrf is disabled and csrf attack is prevented by creating new SessionId in each req.
        http.authorizeHttpRequests(request->request
                .requestMatchers("/user/register","/user/login", "/user/all-user").permitAll() // Allow access without authentication
                    // global roles and permission access
                .requestMatchers("/api/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                    .requestMatchers(GET,"/api/management/**").hasAnyAuthority(ADMIN_READ.name(),MANAGER_READ.name())
                    .requestMatchers(POST,"/api/management/**").hasAnyAuthority(ADMIN_CREATE.name(),MANAGER_CREATE.name())
                    .requestMatchers(PUT,"/api/management/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANAGER_UPDATE.name())
                    .requestMatchers(DELETE,"/api/management/**").hasAnyAuthority(ADMIN_DELETE.name(),MANAGER_DELETE.name())

                                    // OR
                // Annotation Based Authorization can be used (specific controller)
//                .requestMatchers("/api/admin/**").hasRole(ADMIN.name())
//                    .requestMatchers(GET,"/api/admin/**").hasAuthority(ADMIN_READ.name())
//                    .requestMatchers(POST,"/api/admin/**").hasAuthority(ADMIN_CREATE.name())
//                    .requestMatchers(PUT,"/api/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                    .requestMatchers(DELETE,"/api/admin/**").hasAuthority(ADMIN_DELETE.name())

                .anyRequest().authenticated()); // all req should be authenticated. even login form is not accessed
        http.httpBasic(Customizer.withDefaults()); // to access default login form
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // creating unique sessionId for each req
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // basically there are more filters in filterchain we have created a JWT filter to verify the JWT and add this filter before UsernamePasswordAuthenticationFilter
        return http.build();
    }

    @Bean // this bean is used to change the default authentication provider
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // most used authentication provider for DB verification
        provider.setUserDetailsService(userDetailsService); // userdetailservice(Interface) is responsible for verify the user details so we need to create own class adn it is also interface
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12)); // verify the pass after hashing
        return provider;
    }

    // authentication manager is responsible for verifying the login details so we do our custom impl
    // authentication manager calls the authentication provider
    // refer flowchart image in spring learnings folder
    @Bean // Responsible for Login
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
