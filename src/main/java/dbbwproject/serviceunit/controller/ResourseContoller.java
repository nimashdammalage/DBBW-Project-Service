package dbbwproject.serviceunit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public abstract class ResourseContoller {
    final RestTemplate restTemplate;
    final ObjectMapper objectMapper;
    final ModelMapper modelMapper;

    @Value("${firebase.realtime-database-url}")
    protected String fireBaseDBUrl;

    @Value("${firebase.service-account-filename}")
    protected String serviceAccountKeyPath;

    public ResourseContoller(RestTemplate restTemplate, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }
}
