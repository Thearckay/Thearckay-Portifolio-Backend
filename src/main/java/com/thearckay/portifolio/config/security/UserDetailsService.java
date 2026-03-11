package com.thearckay.portifolio.config.security;

import com.thearckay.portifolio.entitys.User;
import com.thearckay.portifolio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userFromDataBase = userRepository.findByEmail(email);
        if (userFromDataBase == null) throw new UsernameNotFoundException("Usuário inválido");
        return userFromDataBase;
    }
}
