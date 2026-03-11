package com.thearckay.portifolio.config.security;

import com.thearckay.portifolio.entitys.User;
import com.thearckay.portifolio.repository.UserRepository;
import com.thearckay.portifolio.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.getTokenfromHeaderRequestOrNull(request);
        if (token != null){
            String emailSubject = jwtService.verifyJwt(token);
            if (emailSubject != null){
                User userFromDataBase = userRepository.findByEmail(emailSubject);

                if (userFromDataBase == null) return;
                Authentication auth = new UsernamePasswordAuthenticationToken(userFromDataBase, null, userFromDataBase.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }


}
