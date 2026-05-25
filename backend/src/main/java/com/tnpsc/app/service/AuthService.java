package com.tnpsc.app.service;

import com.tnpsc.app.dto.AuthRequest;
import com.tnpsc.app.dto.AuthResponse;
import com.tnpsc.app.dto.RegisterRequest;
import com.tnpsc.app.entity.User;
import com.tnpsc.app.repository.UserRepository;
import com.tnpsc.app.config.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User(request.getName(), request.getEmail(), encoder.encode(request.getPassword()));
        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        UserDetails userDetails = loadUserByUsername(request.getEmail());
        if (!encoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtUtils.generateToken(userDetails.getUsername());
        return new AuthResponse(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
