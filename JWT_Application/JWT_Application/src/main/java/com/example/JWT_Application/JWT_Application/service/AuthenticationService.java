package com.example.JWT_Application.JWT_Application.service;

import com.example.JWT_Application.JWT_Application.DTO.AuthenticationRequest;
import com.example.JWT_Application.JWT_Application.DTO.AuthenticationResponse;
import com.example.JWT_Application.JWT_Application.DTO.RegisterRequest;
import com.example.JWT_Application.JWT_Application.JWT.JWTService;
import com.example.JWT_Application.JWT_Application.Repository.UserRepository;
import com.example.JWT_Application.JWT_Application.user.Role;
import com.example.JWT_Application.JWT_Application.user.User;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.function.SupplierUtils;

import java.net.http.HttpHeaders;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
//    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request){
        User user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .role(Role.USER)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        String authPassword = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        String sentPassword = request.getPassword();
        if (!passwordEncoder.matches(sentPassword, authPassword)){
            throw new BadCredentialsException("Password validation failed: Password didn't match");
        }

        User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User with specified email is not present"));
//        String token = jwtService.generateToken(user);


        return AuthenticationResponse.builder().token("Token has been sent in Header").build();

    }

}
