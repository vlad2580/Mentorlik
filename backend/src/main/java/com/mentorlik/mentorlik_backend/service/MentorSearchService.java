package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.elasticsearch._types.FieldValue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for handling mentor search functionality in Mentorlik application.
 * This service interacts with Elasticsearch to perform advanced searches
 * based on expertise, location, rate, and rating of mentors.
 */
@Service
public class MentorSearchService {

    private final ElasticsearchClient client;

    /**
     * Constructs the MentorSearchService with a provided Elasticsearch client.
     *
     * @param client the Elasticsearch client
     */
    public MentorSearchService(ElasticsearchClient client) {
        this.client = client;
    }

    /**
     * Searches mentors based on the specified criteria: expertise, city, country, minimum and maximum rate, and minimum rating.
     *
     * @param expertise the expertise or field of mentorship
     * @param city the city where the mentor is located
     * @param country the country where the mentor is located
     * @param minRate the minimum hourly rate for the mentor
     * @param maxRate the maximum hourly rate for the mentor
     * @param minRating the minimum rating of the mentor
     * @return a list of mentors that match the search criteria
     */
    public List<MentorProfileDto> searchMentors(String expertise, String city, String country, Double minRate, Double maxRate, Double minRating) {
        List<MentorProfileDto> mentors = new ArrayList<>();

        try {
            // Creating search query
            SearchRequest request = SearchRequest.of(b -> b
                .index("mentor_profiles")
                .query(q -> q
                    .bool(bool -> {
                        if (expertise != null && !expertise.isEmpty()) {
                            bool.must(m -> m.term(t -> t.field("expertise.keyword").value(expertise)));
                        }
                        if (city != null && !city.isEmpty()) {
                            bool.must(m -> m.term(t -> t.field("city.keyword").value(city)));
                        }
                        if (country != null && !country.isEmpty()) {
                            bool.must(m -> m.term(t -> t.field("country.keyword").value(country)));
                        }
                        if (minRate != null) {
                            bool.must(m -> m.range(r -> r.field("rate").gte(JsonData.of(minRate))));
                        }
                        if (maxRate != null) {
                            bool.must(m -> m.range(r -> r.field("rate").lte(JsonData.of(maxRate))));
                        }
                        if (minRating != null) {
                            bool.must(m -> m.range(r -> r.field("rating").gte(JsonData.of(minRating))));
                        }
                        return bool;
                    })
                )
                .sort(s -> s
                    .field(f -> f
                        .field("rating")
                        .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                    )
                )
            );
            
            // Executing query and processing results
            @SuppressWarnings("unchecked")
            SearchResponse<Map<String, Object>> response = client.search(request, (Class<Map<String, Object>>) (Class<?>) Map.class);
            
            for (Hit<Map<String, Object>> hit : response.hits().hits()) {
                Map<String, Object> source = hit.source();
                if (source != null) {
                    MentorProfileDto mentorProfile = new MentorProfileDto();
                    mentorProfile.setName((String) source.get("name"));
                    mentorProfile.setExpertise((String) source.get("expertise"));
                    mentorProfile.setCity((String) source.get("city"));
                    mentorProfile.setCountry((String) source.get("country"));
                    mentorProfile.setRating((Double) source.get("rating"));
                    mentors.add(mentorProfile);
                }
            }

        } catch (IOException e) {
            // Log and handle the exception appropriately
            System.err.println("Error executing search: " + e.getMessage());
        }

        return mentors;
    }
    
    /**
     * Поиск менторов по тегам или категориям.
     * 
     * @param tags список тегов для поиска
     * @return список профилей менторов, соответствующих указанным тегам
     */
    public List<MentorProfileDto> searchMentorsByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<MentorProfileDto> mentors = new ArrayList<>();
        
        try {
            // Преобразуем строковые теги в объекты FieldValue
            List<FieldValue> fieldValues = tags.stream()
                    .map(FieldValue::of)
                    .collect(Collectors.toList());
            
            // Создаем поисковый запрос для Elasticsearch по тегам
            SearchRequest request = SearchRequest.of(b -> b
                .index("mentor_profiles")
                .query(q -> q
                    .terms(t -> t
                        .field("tags.keyword")
                        .terms(terms -> terms
                            .value(fieldValues)
                        )
                    )
                )
                .sort(s -> s
                    .field(f -> f
                        .field("rating")
                        .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                    )
                )
            );
            
            // Выполняем запрос и обрабатываем результаты
            @SuppressWarnings("unchecked")
            SearchResponse<Map<String, Object>> response = client.search(request, (Class<Map<String, Object>>) (Class<?>) Map.class);
            
            for (Hit<Map<String, Object>> hit : response.hits().hits()) {
                Map<String, Object> source = hit.source();
                if (source != null) {
                    MentorProfileDto mentorProfile = new MentorProfileDto();
                    mentorProfile.setName((String) source.get("name"));
                    mentorProfile.setExpertise((String) source.get("expertise"));
                    mentorProfile.setCity((String) source.get("city"));
                    mentorProfile.setCountry((String) source.get("country"));
                    mentorProfile.setRating((Double) source.get("rating"));
                    mentors.add(mentorProfile);
                }
            }
            
        } catch (IOException e) {
            // Log and handle the exception appropriately
            System.err.println("Error executing tag search: " + e.getMessage());
        }
        
        return mentors;
    }
}
