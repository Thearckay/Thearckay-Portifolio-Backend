package com.thearckay.portifolio.service;

import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.dto.LoginRequest;
import com.thearckay.portifolio.dto.Token;
import com.thearckay.portifolio.entitys.User;
import com.thearckay.portifolio.exceptions.LoginException;
import com.thearckay.portifolio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<?> loginVerification(LoginRequest loginRequest){
        try {
            User user = userRepository.findByEmail(loginRequest.email());

            if (user == null) throw new LoginException("O Usuário está incorreto", HttpStatus.BAD_REQUEST.value());
            if (!user.getPassword().equals(loginRequest.password())) throw new LoginException("Senha incorreta", HttpStatus.FORBIDDEN.value());

            Token token = jwtService.createJwt(user.getEmail());

            ApiResponse response = new ApiResponse(
                    HttpStatus.OK.value(),
                    token,
                    "Logado com Sucesso!",
                    LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (LoginException e) {
            throw new LoginException(e.getMessage(), e.getStatus());
        }
    }
}
