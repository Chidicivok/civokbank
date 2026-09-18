package com.chidicivok.civokbank.services.implementations;


import com.chidicivok.civokbank.DTOs.requests.LoginRequest;
import com.chidicivok.civokbank.services.JwtService;
import com.chidicivok.civokbank.services.interfaces.AuthService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImplementation(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Override
    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return jwtService.generateToken(request.getEmail());
    }
}


