package com.mentorlik.mentorlik_backend.service.auth.service;

import com.mentorlik.mentorlik_backend.dto.auth.AuthRequestDto;
import com.mentorlik.mentorlik_backend.dto.profile.AdminProfileDto;
import com.mentorlik.mentorlik_backend.dto.profile.UserDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.AdminProfile;
import com.mentorlik.mentorlik_backend.repository.AdminProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for handling admin authentication and profile management.
 * Implements the base {@link BaseAuthService} to provide admin-specific logic for registration and login.
 */
@Service
public class AdminAuthService implements BaseAuthService<AdminProfile, AdminProfileDto> {

    private final AdminProfileRepository adminProfileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an instance of {@code AdminAuthService} with the required dependencies.
     *
     * @param adminProfileRepository The repository for managing admin profiles.
     * @param passwordEncoder The encoder for hashing passwords.
     */
    @Autowired
    public AdminAuthService(AdminProfileRepository adminProfileRepository, PasswordEncoder passwordEncoder) {
        this.adminProfileRepository = adminProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Выполняет вход администратора с использованием email и пароля.
     *
     * @param authRequest DTO запроса аутентификации, содержащий email и пароль
     * @return DTO администратора после успешной аутентификации
     * @throws ResourceNotFoundException если администратор не найден или данные неверны
     */
    @Override
    public AdminProfileDto login(AuthRequestDto authRequest) {
        AdminProfile admin = adminProfileRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + authRequest.getEmail()));

        if (!passwordEncoder.matches(authRequest.getPassword(), admin.getPassword())) {
            throw new ResourceNotFoundException("Invalid credentials provided");
        }

        return convertToDto(admin);
    }

    /**
     * OAuth2 аутентификация не поддерживается для администраторов.
     *
     * @param token токен OAuth2 
     * @param userType тип пользователя
     * @return никогда не возвращает результат, всегда выбрасывает исключение
     * @throws UnsupportedOperationException всегда выбрасывает это исключение
     */
    @Override
    public AdminProfileDto loginWithToken(String token, String userType) {
        throw new UnsupportedOperationException("OAuth2 login is not supported for admin users");
    }

    /**
     * Регистрирует нового администратора.
     *
     * @param userDto DTO с данными администратора
     * @return DTO зарегистрированного администратора
     * @throws EmailAlreadyExistsException если email уже используется
     */
    @Override
    public AdminProfileDto register(UserDto userDto) {
        if (adminProfileRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        AdminProfileDto adminDto = (AdminProfileDto) userDto;
        AdminProfile admin = createUserEntity(adminDto);
        adminProfileRepository.save(admin);

        return convertToDto(admin);
    }

    /**
     * Creates a new {@link AdminProfile} entity from the provided {@link AdminProfileDto}.
     *
     * @param userDto The DTO containing admin profile data.
     * @return A new {@link AdminProfile} entity with the data from the DTO.
     */
    protected AdminProfile createUserEntity(AdminProfileDto userDto) {
        AdminProfile admin = new AdminProfile();
        admin.setEmail(userDto.getEmail());
        admin.setPassword(passwordEncoder.encode(userDto.getPassword()));
        admin.setRole(userDto.getRole());
        admin.setAccessLevel(userDto.getAccessLevel());
        admin.setDescription(userDto.getDescription());
        return admin;
    }

    /**
     * Converts an {@link AdminProfile} entity to an {@link AdminProfileDto}.
     *
     * @param admin The admin entity to convert.
     * @return An {@link AdminProfileDto} containing the data from the entity.
     */
    protected AdminProfileDto convertToDto(AdminProfile admin) {
        return AdminProfileDto.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .role(admin.getRole())
                .accessLevel(admin.getAccessLevel())
                .description(admin.getDescription())
                .build();
    }
}