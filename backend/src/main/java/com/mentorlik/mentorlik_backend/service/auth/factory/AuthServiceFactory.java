package com.mentorlik.mentorlik_backend.service.auth.factory;

import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.model.User;
import com.mentorlik.mentorlik_backend.service.auth.service.BaseAuthService;
import com.mentorlik.mentorlik_backend.service.auth.service.OAuth2AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

/**
 * Factory class to provide the appropriate authentication service based on user type.
 */
@Slf4j
@Component
public class AuthServiceFactory {

    private final Map<String, BaseAuthService<? extends User, ? extends UserDto>> authServiceMap;

    /**
     * Constructs an instance of {@code AuthServiceFactory} with a map of available auth services.
     *
     * @param authServiceMap a map of available auth services keyed by user type
     */
    @Autowired
    public AuthServiceFactory(Map<String, BaseAuthService<? extends User, ? extends UserDto>> authServiceMap) {
        this.authServiceMap = authServiceMap;
        log.info("AuthServiceFactory initialized with user types: {}", authServiceMap.keySet());
    }

    /**
     * Retrieves the appropriate {@link BaseAuthService} instance based on the user type.
     *
     * @param userType the type of user for which authentication service is needed (e.g., "admin", "mentor", "student", "google", "linkedin")
     * @return the {@code BaseAuthService} associated with the specified user type
     * @throws IllegalArgumentException if the user type is not recognized
     */
    public BaseAuthService<? extends User, ? extends UserDto> getAuthService(String userType) {
        if (userType == null || userType.trim().isEmpty()) {
            log.error("Invalid user type: user type is null or empty");
            throw new IllegalArgumentException("User type must not be null or empty");
        }

        return Optional.ofNullable(authServiceMap.get(userType.toLowerCase()))
                .orElseThrow(() -> {
                    log.warn("Invalid user type specified: {}", userType);
                    return new IllegalArgumentException("Invalid user type: " + userType);
                });
    }

    /**
     * Processes login with OAuth2 token using the specified user type.
     *
     * @param userType the type of user for which authentication service is needed (e.g., "google", "linkedin")
     * @param oauthToken the OAuth2 token used for authentication
     * @return the {@link UserDto} of the authenticated user
     * @throws IllegalArgumentException if the user type is not recognized
     */
    public UserDto loginWithOAuth(String userType, String oauthToken) {
        BaseAuthService<? extends User, ? extends UserDto> authService = getAuthService(userType);
        if (authService instanceof OAuth2AuthService) {
            return ((OAuth2AuthService) authService).loginWithOAuth(userType, oauthToken);
        } else {
            throw new IllegalArgumentException("OAuth2 login not supported for user type: " + userType);
        }
    }
}