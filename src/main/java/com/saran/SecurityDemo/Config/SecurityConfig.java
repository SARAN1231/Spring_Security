package com.saran.SecurityDemo.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // saying it is Config File
@EnableWebSecurity // Don't go with default spring security config . Go with my custom config
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer->customizer.disable()); // in default spring security create csrf token in eah req to prevent csrf attacks. In this case csrf is disabled and csrf attack is prevented by creating new SessionId in each req.
        http.authorizeHttpRequests(request->request.anyRequest().authenticated()); // all req should be authenticated even login form is not accessed
        http.httpBasic(Customizer.withDefaults()); // to access default login form
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // creating unique sessionId for each req
        return http.build();
    }
}
