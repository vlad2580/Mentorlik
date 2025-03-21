package com.mentorlik.mentorlik_backend.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host:localhost}")
    private String host;

    @Value("${elasticsearch.port:9200}")
    private int port;

    @Value("${elasticsearch.protocol:http}")
    private String protocol;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, protocol))
        );
    }
    
    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost(host, port, protocol)).build();
    }
    
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Creating transport with Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient(), new JacksonJsonpMapper());
        
        // Creating API client
        return new ElasticsearchClient(transport);
    }
} 