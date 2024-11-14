package com.mentorlik.mentorlik_backend.controller;

import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import com.mentorlik.mentorlik_backend.service.MentorSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing mentor search functionality in the Mentorlik application.
 * This controller provides endpoints to search mentors based on various criteria such as
 * expertise, location, rate, and rating.
 */
@RestController
@RequestMapping("/api/mentors")
public class MentorSearchController {

    private final MentorSearchService mentorSearchService;

    /**
     * Constructs the MentorSearchController with the provided MentorSearchService.
     *
     * @param mentorSearchService the service for handling mentor search operations
     */
    public MentorSearchController(MentorSearchService mentorSearchService) {
        this.mentorSearchService = mentorSearchService;
    }

    /**
     * Searches mentors based on specified query parameters, such as expertise, location, rate, and rating.
     * All parameters are optional, allowing flexibility in searches.
     *
     * @param expertise the expertise or field of mentorship
     * @param city the city where the mentor is located
     * @param country the country where the mentor is located
     * @param minRate the minimum hourly rate for the mentor
     * @param maxRate the maximum hourly rate for the mentor
     * @param minRating the minimum rating of the mentor
     * @return a ResponseEntity containing a list of MentorProfileDto objects matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<MentorProfileDto>> searchMentors(
            @RequestParam(required = false) String expertise,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Double minRate,
            @RequestParam(required = false) Double maxRate,
            @RequestParam(required = false) Double minRating) {

        List<MentorProfileDto> mentors = mentorSearchService.searchMentors(expertise, city, country, minRate, maxRate, minRating);
        return ResponseEntity.ok(mentors);
    }
}