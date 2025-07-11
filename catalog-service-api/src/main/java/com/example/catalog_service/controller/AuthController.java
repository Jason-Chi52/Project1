package com.example.catalog_service.controller;

import com.example.catalog_service.model.User;
import com.example.catalog_service.repo.UserRepository;
import com.example.catalog_service.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Signup and login endpoints. Returns a JWT on success.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepo,
                          PasswordEncoder passwordEncoder) {
        this.authManager     = authManager;
        this.jwtUtil         = jwtUtil;
        this.userRepo        = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user. Returns { "token": "<jwt>" }.
     */
    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody Map<String, String> body) {
        String uname = body.get("username");
        String pwd   = body.get("password");

        if (userRepo.findByUsername(uname).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User u = new User();
        u.setUsername(uname);
        // Use injected PasswordEncoder, not JwtUtil
        u.setPassword(passwordEncoder.encode(pwd));
        userRepo.save(u);

        String token = jwtUtil.generateToken(uname);
        return Map.of("token", token);
    }

    /**
     * Authenticate an existing user. Returns { "token": "<jwt>" }.
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String uname = body.get("username");
        String pwd   = body.get("password");

        // This will throw BadCredentialsException if invalid
        authManager.authenticate(new UsernamePasswordAuthenticationToken(uname, pwd));

        String token = jwtUtil.generateToken(uname);
        return Map.of("token", token);
    }
}
