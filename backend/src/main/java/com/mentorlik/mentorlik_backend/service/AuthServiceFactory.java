package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.UserDto;
import com.mentorlik.mentorlik_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory class to provide the appropriate authentication service based on user type.
 */
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
    }

    /**
     * Returns the appropriate {@link BaseAuthService} based on the user type.
     *
     * @param userType the type of the user (e.g., "admin", "mentor", "student")
     * @return the authentication service for the specified user type
     * @throws IllegalArgumentException if the user type is not recognized
     */
    public BaseAuthService<? extends User, ? extends UserDto> getAuthService(String userType) {
        BaseAuthService<? extends User, ? extends UserDto> authService = authServiceMap.get(userType.toLowerCase());
        if (authService == null) {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
        return authService;
    }
}