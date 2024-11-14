package com.mentorlik.mentorlik_backend.service;

import com.mentorlik.mentorlik_backend.dto.profile.MentorProfileDto;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling mentor search functionality in Mentorlik application.
 * This service interacts with Elasticsearch to perform advanced searches
 * based on expertise, location, rate, and rating of mentors.
 */
@Service
public class MentorSearchService {

    private final RestHighLevelClient client;

    /**
     * Constructs the MentorSearchService with a provided Elasticsearch client.
     *
     * @param client the Elasticsearch high-level REST client
     */
    public MentorSearchService(RestHighLevelClient client) {
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
            SearchRequest searchRequest = new SearchRequest("mentor_profiles");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // Building a boolean query with multiple conditions
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            if (expertise != null && !expertise.isEmpty()) {
                queryBuilder.must(QueryBuilders.matchQuery("expertise", expertise));
            }
            if (city != null && !city.isEmpty()) {
                queryBuilder.must(QueryBuilders.matchQuery("city", city));
            }
            if (country != null && !country.isEmpty()) {
                queryBuilder.must(QueryBuilders.matchQuery("country", country));
            }
            if (minRate != null) {
                queryBuilder.must(QueryBuilders.rangeQuery("rate").gte(minRate));
            }
            if (maxRate != null) {
                queryBuilder.must(QueryBuilders.rangeQuery("rate").lte(maxRate));
            }
            if (minRating != null) {
                queryBuilder.must(QueryBuilders.rangeQuery("rating").gte(minRating));
            }

            sourceBuilder.query(queryBuilder);
            sourceBuilder.sort("rating", SortOrder.DESC);
            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();

            hits.forEach(hit -> {
                MentorProfileDto mentorProfile = new MentorProfileDto();
                mentorProfile.setName((String) hit.getSourceAsMap().get("name"));
                mentorProfile.setExpertise((String) hit.getSourceAsMap().get("expertise"));
                mentorProfile.setCity((String) hit.getSourceAsMap().get("city"));
                mentorProfile.setCountry((String) hit.getSourceAsMap().get("country"));
                mentorProfile.setRating(BigDecimal.valueOf((Double) hit.getSourceAsMap().get("rating")));
                mentors.add(mentorProfile);
            });

        } catch (IOException e) {
            // Log and handle the exception appropriately
            System.err.println("Error executing search: " + e.getMessage());
        }

        return mentors;
    }
}
