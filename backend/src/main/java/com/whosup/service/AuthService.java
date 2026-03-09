package com.whosup.service;

import com.whosup.config.JwtTokenProvider;
import com.whosup.dto.request.LoginRequest;
import com.whosup.dto.request.RegisterRequest;
import com.whosup.dto.response.AuthResponse;
import com.whosup.entity.User;
import com.whosup.exception.BadRequestException;
import com.whosup.mapper.UserMapper;
import com.whosup.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.displayName()
        );
        user = userRepository.save(user);

        String token = tokenProvider.generateToken(user.getId());
        return new AuthResponse(token, UserMapper.toResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = tokenProvider.generateToken(user.getId());
        return new AuthResponse(token, UserMapper.toResponse(user));
    }
}
