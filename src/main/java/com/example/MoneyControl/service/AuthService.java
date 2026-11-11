package com.example.MoneyControl.service;

import com.example.MoneyControl.dto.AuthResponse;
import com.example.MoneyControl.dto.LoginRequest;
import com.example.MoneyControl.dto.RegisterRequest;
import com.example.MoneyControl.entity.User;
import com.example.MoneyControl.exception.ConflictException;
import com.example.MoneyControl.exception.InvalidCredentialsException;
import com.example.MoneyControl.repository.UserRepository;
import com.example.MoneyControl.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Este e-mail já está cadastrado");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(email), user.getName());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return new AuthResponse(jwtService.generateToken(email), user.getName());
    }
}