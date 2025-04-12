package com.mentorlik.mentorlik_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ReactiveElasticsearchClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication(exclude = {
    ElasticsearchRestClientAutoConfiguration.class,
    ReactiveElasticsearchClientAutoConfiguration.class,
    ElasticsearchClientAutoConfiguration.class
})
public class MentorlikBackendApplication {
	private static final Logger logger = LoggerFactory.getLogger(MentorlikBackendApplication.class);

	public static void main(String[] args) {
		logger.info("Starting MentorlikBackend Application...");
		SpringApplication.run(MentorlikBackendApplication.class, args);
	}
}