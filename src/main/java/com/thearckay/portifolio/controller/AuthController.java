package com.thearckay.portifolio.controller;

import com.thearckay.portifolio.dto.ApiResponse;
import com.thearckay.portifolio.dto.LoginRequest;
import com.thearckay.portifolio.service.JwtService;
import com.thearckay.portifolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyToken(@PathVariable("token") String token){
        String subject = jwtService.verifyJwt(token);

        if (subject == null){
            System.out.println("O conteúdo é nulo");
            return ResponseEntity.badRequest().body(new ApiResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    Collections.emptyList(),
                    "Token inválido",
                    LocalDateTime.now()
            ));
        }

        return ResponseEntity.status(200).body(new ApiResponse(
                200,
                Collections.emptyList(),
                "Tudo certo!",
                LocalDateTime.now()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLoginAuthenticate(@RequestBody LoginRequest loginRequest){
        return userService.loginVerification(loginRequest);
    }

}
