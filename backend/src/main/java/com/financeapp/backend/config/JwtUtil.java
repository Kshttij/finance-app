package com.financeapp.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Interview Point: Explain this class.
 *
 * "This is a utility class for handling all JWT operations.
 * It's responsible for three main things:
 * 1. Generating a new token when a user logs in.
 * 2. Extracting information (like username) from a token.
 * 3. Validating a token (checking its signature and expiration)."
 */
@Component
public class JwtUtil {

    // Set a validity period (e.g., 10 hours in milliseconds)
    private static final long TOKEN_VALIDITY = 1000 * 60 * 60 * 10;

    // Use a strong, secure key.
    // In a real app, this would be loaded from application.properties
    // @Value("${jwt.secret}")
    // private String secret;
    // For this project, we'll generate a secure key.
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // --- Public Methods (used by other services) ---

    /**
     * Extracts the username (the "subject") from the JWT.
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Generates a new JWT for a given user.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // We could add more data to the token here, like roles
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Validates a token.
     * Checks if the username matches and if the token is expired.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        boolean isUsernameMatch = username.equals(userDetails.getUsername());
        boolean isTokenNotExpired = !isTokenExpired(token);

        return isUsernameMatch && isTokenNotExpired;
    }

    // --- Private Helper Methods ---

    /**
     * A generic function to extract any "claim" from the token.
     * (A claim is just a piece of data, like "subject" or "expiration").
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This does the actual parsing of the token using the secret key.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the token's expiration date is before the current time.
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /**
     * This is the main token-building logic.
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // The "subject" is the user (their username)
                .setIssuedAt(new Date(currentTimeMillis)) // Set when it was created
                .setExpiration(new Date(currentTimeMillis + TOKEN_VALIDITY)) // Set expiration
                .signWith(SECRET_KEY) // Sign it with our secret key
                .compact(); // Build the string
    }
}