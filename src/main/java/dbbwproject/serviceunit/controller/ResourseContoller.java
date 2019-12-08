package dbbwproject.serviceunit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public abstract class ResourseContoller {
    protected final RestTemplate restTemplate;

    @Value("${firebase.realtime-database-url}")
    protected String fireBaseDBUrl;

    @Value("${firebase.service-account-filename}")
    protected String serviceAccountKeyPath;

    protected ResourseContoller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
