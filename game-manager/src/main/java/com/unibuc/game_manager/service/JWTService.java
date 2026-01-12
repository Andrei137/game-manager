package com.unibuc.game_manager.service;

import com.unibuc.game_manager.exception.UnauthorizedException;
import com.unibuc.game_manager.model.Customer;
import com.unibuc.game_manager.model.Provider;
import com.unibuc.game_manager.model.User;
import com.unibuc.game_manager.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Optional;

@Service
public final class JWTService {

    private final String secretKey;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public JWTService(@Value("${security.jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
        System.out.println("Admin Token: " + getToken("admin"));
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String userId && !userId.equals("anonymousUser")) {
            return userId;
        }
        throw new UnauthorizedException();
    }

    public String getToken(String id) {
        return Jwts
                .builder()
                .claim("userId", id)
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);
    }

    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean isPasswordValid(String providedPassword, String actualPassword) {
        return passwordEncoder.matches(providedPassword, actualPassword);
    }

    public void checkAdmin() {
        if (!getUserId().equals("admin")) throw new UnauthorizedException();
    }

    public User getCurrentUser() {
        Optional<User> user = userRepository.findById(Integer.parseInt(getUserId()));
        if (user.isEmpty()) throw new UnauthorizedException();
        return user.get();
    }

    public Customer getCurrentCustomer() {
        if (getCurrentUser() instanceof Customer customer) return customer;
        return null;
    }

    public Provider getCurrentProvider() {
        if (getCurrentUser() instanceof Provider provider) return provider;
        return null;
    }

}