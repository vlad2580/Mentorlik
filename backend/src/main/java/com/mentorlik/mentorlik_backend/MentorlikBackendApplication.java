package com.mentorlik.mentorlik_backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MentorlikBackendApplication {
	public static void main(String[] args) {
		log.info("Starting MentorlikBackend Application...");
		SpringApplication.run(MentorlikBackendApplication.class, args);
		log.info("MentorlikBackend Application started successfully.");
	}
}