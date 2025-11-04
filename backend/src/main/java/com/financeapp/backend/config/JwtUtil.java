package com.financeapp.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority; // <-- Import this
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors; // <-- Import this

@Component
public class JwtUtil {

    private static final long TOKEN_VALIDITY = 1000 * 60 * 60 * 10;
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    
     // Extracts the username (the "subject") from the JWT.
    
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // --- SIMPLE METHOD ---
    /**
     * Generates a new JWT for a given username.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // No roles are added in this simple version
        return doGenerateToken(claims, username);
    }

    // --- FOR RBAC ---
    /**
     * standard Spring way
     * Generates a new JWT from UserDetails, including roles.
     * used mainly for adding ROLES to jwt
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        var roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        
        claims.put("roles", roles);
        
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Validates a token.
     * (This method remains unchanged, it works fine)
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        boolean isUsernameMatch = username.equals(userDetails.getUsername());
        boolean isTokenNotExpired = !isTokenExpired(token);

        return isUsernameMatch && isTokenNotExpired;
    }

    // --- Private Helper Methods (Unchanged) ---

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        
        return Jwts.builder()
                .setClaims(claims) 
                .setSubject(subject)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + TOKEN_VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }
}