package com.mentorlik.mentorlik_backend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleConfig {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() throws GeneralSecurityException, IOException {
        return new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID"))
                .build();
    }
}