package com.thearckay.portifolio.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.thearckay.portifolio.dto.Token;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    private Algorithm algorithm;
    @Value("${portifolio.secretkey}")
    private String secretKey;
    @Value("${portifolio.issuer}")
    private String issuer;

    @PostConstruct
    private void init(){
        algorithm = Algorithm.HMAC256(secretKey);
    }


    public Token createJwt(String userEmail){
        try{
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withExpiresAt(getWhenExpires())
                    .withSubject(userEmail)
                    .sign(algorithm);

            return new Token(token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao criar o JWT");
        }
    }

    public String verifyJwt(Token token, String email){
        try {
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token.getToken())
                    .getSubject();

        } catch (RuntimeException e) {
            return null;
        }
    }

    public String verifyJwt(String token){
        try {
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (RuntimeException e) {
            return null;
        }
    }

    public String getTokenfromHeaderRequestOrNull(HttpServletRequest httpRequest){
        String header = httpRequest.getHeader("Authorization");
        if (header == null) return null;
        return header.replace("Bearer ", "");
    }

    public Instant getWhenExpires(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
