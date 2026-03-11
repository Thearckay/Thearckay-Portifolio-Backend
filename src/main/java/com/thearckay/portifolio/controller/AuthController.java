package com.thearckay.portifolio.controller;

import com.thearckay.portifolio.dto.LoginRequest;
import com.thearckay.portifolio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> userLoginAuthenticate(@RequestBody LoginRequest loginRequest){
        return userService.loginVerification(loginRequest);
    }

}
