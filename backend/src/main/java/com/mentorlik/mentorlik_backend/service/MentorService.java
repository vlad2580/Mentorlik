package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.exception.ResourceNotFoundException;
import com.mentorlik.mentorlik_backend.exception.validation.EmailAlreadyExistsException;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.repository.MentorProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing mentor data.
 * <p>
 * Provides methods for working with mentor profiles,
 * including creation, search, update, and deletion.
 * </p>
 */
@Slf4j
@Service
public class MentorService {

    private final MentorProfileRepository mentorProfileRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates an instance of the mentor service.
     *
     * @param mentorProfileRepository repository for accessing mentor data
     * @param passwordEncoder password encoder
     */
    public MentorService(MentorProfileRepository mentorProfileRepository, PasswordEncoder passwordEncoder) {
        this.mentorProfileRepository = mentorProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gets a list of all mentors.
     *
     * @return list of mentor DTOs
     */
    @Transactional(readOnly = true)
    public List<MentorProfileDto> getAllMentors() {
        log.info("Getting all mentors");
        return mentorProfileRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Gets a mentor by ID.
     *
     * @param id mentor identifier
     * @return mentor DTO
     * @throws ResourceNotFoundException if a mentor with the specified ID is not found
     */
    @Transactional(readOnly = true)
    public MentorProfileDto getMentorById(Long id) {
        log.info("Getting mentor with ID: {}", id);
        MentorProfile mentor = mentorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + id));
        return convertToDto(mentor);
    }

    /**
     * Creates a new mentor.
     *
     * @param mentorDto data for the new mentor
     * @return DTO of the created mentor
     * @throws EmailAlreadyExistsException if the email is already in use
     */
    @Transactional
    public MentorProfileDto createMentor(MentorProfileDto mentorDto) {
        log.info("Creating new mentor with email: {}", mentorDto.getEmail());
        
        // Check email uniqueness
        if (mentorProfileRepository.findByEmail(mentorDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use: " + mentorDto.getEmail());
        }
        
        MentorProfile mentor = convertToEntity(mentorDto);
        
        // Password hashing
        if (mentorDto.getPassword() != null && !mentorDto.getPassword().isEmpty()) {
            mentor.setPassword(passwordEncoder.encode(mentorDto.getPassword()));
        }
        
        MentorProfile savedMentor = mentorProfileRepository.save(mentor);
        log.info("Mentor successfully created with ID: {}", savedMentor.getId());
        return convertToDto(savedMentor);
    }

    /**
     * Updates mentor data.
     *
     * @param id mentor identifier
     * @param mentorDto new mentor data
     * @return DTO of the updated mentor
     * @throws ResourceNotFoundException if a mentor with the specified ID is not found
     * @throws EmailAlreadyExistsException if the new email is already used by another user
     */
    @Transactional
    public MentorProfileDto updateMentor(Long id, MentorProfileDto mentorDto) {
        log.info("Updating mentor with ID: {}", id);
        
        MentorProfile existingMentor = mentorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with ID: " + id));
        
        // Check that the new email is not used by another user
        if (!existingMentor.getEmail().equals(mentorDto.getEmail()) && 
                mentorProfileRepository.findByEmail(mentorDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use: " + mentorDto.getEmail());
        }
        
        // Update fields
        updateMentorFields(existingMentor, mentorDto);
        
        MentorProfile updatedMentor = mentorProfileRepository.save(existingMentor);
        log.info("Mentor successfully updated with ID: {}", updatedMentor.getId());
        return convertToDto(updatedMentor);
    }

    /**
     * Deletes a mentor by ID.
     *
     * @param id mentor identifier
     * @throws ResourceNotFoundException if a mentor with the specified ID is not found
     */
    @Transactional
    public void deleteMentor(Long id) {
        log.info("Deleting mentor with ID: {}", id);
        
        if (!mentorProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mentor not found with ID: " + id);
        }
        
        mentorProfileRepository.deleteById(id);
        log.info("Mentor successfully deleted with ID: {}", id);
    }

    /**
     * Searches for mentors by the given query.
     *
     * @param query search string
     * @return list of found mentor DTOs
     */
    @Transactional(readOnly = true)
    public List<MentorProfileDto> searchMentors(String query) {
        log.info("Searching mentors by query: {}", query);
        
        // A more complex search could be implemented here, for example using Specification
        List<MentorProfile> mentors = mentorProfileRepository.findByNameContainingIgnoreCaseOrExpertiseContainingIgnoreCase(
                query, query);
        
        return mentors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a mentor entity to DTO.
     *
     * @param mentor mentor entity
     * @return mentor DTO
     */
    private MentorProfileDto convertToDto(MentorProfile mentor) {
        MentorProfileDto dto = new MentorProfileDto();
        dto.setId(mentor.getId());
        dto.setEmail(mentor.getEmail());
        dto.setName(mentor.getName());
        dto.setExpertise(mentor.getExpertise());
        dto.setRate(mentor.getRate());
        dto.setRating(mentor.getRating());
        dto.setAbout(mentor.getAbout());
        dto.setPhotoUrl(mentor.getPhotoUrl());
        dto.setAvailability(mentor.getAvailability());
        // Not transferring password to DTO
        
        return dto;
    }

    /**
     * Converts a mentor DTO to entity.
     *
     * @param mentorDto mentor DTO
     * @return mentor entity
     */
    private MentorProfile convertToEntity(MentorProfileDto mentorDto) {
        MentorProfile mentor = new MentorProfile();
        mentor.setId(mentorDto.getId()); // Null for new mentors
        mentor.setEmail(mentorDto.getEmail());
        mentor.setName(mentorDto.getName());
        mentor.setExpertise(mentorDto.getExpertise());
        mentor.setRate(mentorDto.getRate());
        mentor.setRating(mentorDto.getRating());
        mentor.setAbout(mentorDto.getAbout());
        mentor.setPhotoUrl(mentorDto.getPhotoUrl());
        mentor.setAvailability(mentorDto.getAvailability());
        
        return mentor;
    }

    /**
     * Updates fields of an existing mentor based on DTO.
     *
     * @param existingMentor existing mentor entity
     * @param mentorDto DTO with new data
     */
    private void updateMentorFields(MentorProfile existingMentor, MentorProfileDto mentorDto) {
        existingMentor.setEmail(mentorDto.getEmail());
        existingMentor.setName(mentorDto.getName());
        existingMentor.setExpertise(mentorDto.getExpertise());
        existingMentor.setRate(mentorDto.getRate());
        existingMentor.setAbout(mentorDto.getAbout());
        existingMentor.setPhotoUrl(mentorDto.getPhotoUrl());
        existingMentor.setAvailability(mentorDto.getAvailability());
        
        // Update password only if specified in DTO
        if (mentorDto.getPassword() != null && !mentorDto.getPassword().isEmpty()) {
            existingMentor.setPassword(passwordEncoder.encode(mentorDto.getPassword()));
        }
    }
} 