package com.example.bridge.security;

import com.example.bridge.constants.RoleEnum;
import com.example.bridge.dtos.User;
import com.example.bridge.dtos.requests.AuthenticationRequest;
import com.example.bridge.dtos.requests.RegisterRequest;
import com.example.bridge.dtos.responses.AuthenticationResponse;
import com.example.bridge.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .role(RoleEnum.ADMIN)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);

        Map<String, Object> payload = new HashMap<>();
        payload.put("roles", RoleEnum.ADMIN);

        var jwtToken = jwtService.generateToken(payload, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        Map<String, Object> payload = new HashMap<>();
        payload.put("roles", RoleEnum.ADMIN);

        var jwtToken = jwtService.generateToken(payload, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
