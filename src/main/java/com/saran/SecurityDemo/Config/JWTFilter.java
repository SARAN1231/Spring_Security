package com.saran.SecurityDemo.Config;

import com.saran.SecurityDemo.Services.JWTService;
import com.saran.SecurityDemo.Services.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter { //  JWTFilter acts as filter class when parent filter class extends it

    private final JWTService jwtService;
    @Autowired
    ApplicationContext context;
    public JWTFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdW5pbCIsImlhdCI6MTczOTk2NjM2NCwiZXhwIjoxNzM5OTY2NDcyfQ.etrF4tkzWVV2wW1qnS8FjHiOWaONVAydYUVF3KxOpxQ

      String AuthHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;
      if (AuthHeader != null && AuthHeader.startsWith("Bearer ")) {
           token = AuthHeader.substring(7);
          username = jwtService.extractUserName(token);
      }

      if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // if username is not null and the user is not authenticated then proceed
          UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(username);

          if(jwtService.validateToken(token,userDetails) ) {
              UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // setting the token with request object
              SecurityContextHolder.getContext().setAuthentication(authToken); // adding to SecurityContextHolder will says that this user is authenticated
          }
      }
      filterChain.doFilter(request, response); // continue with other upcoming filter
    }
}
