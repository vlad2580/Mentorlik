package com.mentorlik.mentorlik_backend.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Elasticsearch, которая будет отключена
 * так как spring.autoconfigure.exclude не всегда работает должным образом
 */
@Configuration
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "false", matchIfMissing = true)
public class ElasticsearchConfiguration {
    // Пустая конфигурация для отключения Elasticsearch
} 