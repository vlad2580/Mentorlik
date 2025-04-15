package com.mentorlik.mentorlik_backend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Configuration for JWT tokens.
 * <p>
 * This configuration sets up the parameters for JWT tokens used for authentication,
 * including the secret key, token lifetime, and other parameters.
 * </p>
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret:default_secret_key_for_development_only}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours by default
    private long expirationTime;

    @Value("${jwt.refresh-expiration:604800000}") // 7 days by default
    private long refreshExpirationTime;

    /**
     * Creates a secure secret key for signing JWT tokens.
     * Uses Keys.secretKeyFor to ensure proper key size for HS512 algorithm.
     *
     * @return secure secret key
     */
    @Bean
    public SecretKey jwtSecretKey() {
        // Generate a secure key with proper size for HS512 algorithm
        // This ensures the key size is at least 512 bits as required by JWT spec
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    /**
     * Gets the token lifetime in milliseconds.
     *
     * @return token lifetime
     */
    public long getExpirationTime() {
        return expirationTime;
    }

    /**
     * Gets the refresh token lifetime in milliseconds.
     *
     * @return refresh token lifetime
     */
    public long getRefreshExpirationTime() {
        return refreshExpirationTime;
    }
} 