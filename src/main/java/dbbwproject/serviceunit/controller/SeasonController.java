package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.response.ErrStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import dbbwproject.serviceunit.firebasehandler.AccessTokenGenrator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Season Management", description = "handling season resource operations")
@RequestMapping("/resource-management")
public class SeasonController extends ResourseContoller {
    private static final String SEASONS_PATH = "/seasons";

    @Autowired
    public SeasonController(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @GetMapping("/season-status")
    @ApiOperation(value = "Retrieve a list of all season status", response = ResponseWrapperList.class)
    public ResponseWrapperList<SeasonStatus> getAllSeasonStatus() {
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, Arrays.asList(SeasonStatus.values()));
    }

    @GetMapping("/seasons")
    @ApiOperation(value = "Retrieve a list of all seasons", response = ResponseWrapperList.class)
    public ResponseWrapperList<SeasonDTO> getAllSeasons() {
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + SEASONS_PATH + ".json?access_token=" + accessToken;
        Map<String, SeasonDTO> result = restTemplate.getForObject(url, Map.class);
        if (result == null) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, "error in getAllSeasons");
        }
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, new ArrayList<>(result.values()), null);
    }

    @GetMapping("/seasons/{code}")
    @ApiOperation(value = "Retrieve season by code", response = ResponseWrapper.class)
    public ResponseWrapper<SeasonDTO> getSeasonByCode(@PathVariable String code) {
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + SEASONS_PATH + "/" + code + ".json?access_token=" + accessToken;
        ResponseEntity<SeasonDTO> result = restTemplate.getForEntity(url, SeasonDTO.class);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "error in call firebase get method" + result.getBody().toString());
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, result.getBody());
    }

    @ApiOperation(value = "Modify existing season by code", response = ResponseWrapper.class)
    @PutMapping("/seasons/{code}")
    public ResponseWrapper<SeasonDTO> modifySeasonByCode(@PathVariable String code, @RequestBody SeasonDTO resource) {
        if (!code.equals(resource.getCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season's code: " + resource.getCode() + "and code in url: " + code + " does not match");
        }
        if (getSeasonByCode(code).getResponseObject() == null) {
            //Season not exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season with code: " + resource.getCode() + " does not exist in DB for modification");
        }

        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + SEASONS_PATH + "/" + resource.getCode() + ".json?access_token=" + accessToken;
        restTemplate.put(url, resource, SeasonDTO.class);
        SeasonDTO updatedSeason = getSeasonByCode(code).getResponseObject();
        if (updatedSeason == null) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "error in calling in firebase API");
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, updatedSeason);
    }

    @ApiOperation(value = "Create a season ", response = ResponseWrapper.class)
    @PostMapping("/seasons")
    public ResponseWrapper<SeasonDTO> createNewSeason(@RequestBody SeasonDTO resource) {
        if (getSeasonByCode(resource.getCode()).getResponseObject() != null) {
            //Season already exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season with code: " + resource.getCode() + " already exists");
        }
        if (resource.getCode() == null || resource.getCode().isEmpty()) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season code can not be empty or null");
        }

        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + SEASONS_PATH + "/" + resource.getCode() + ".json?access_token=" + accessToken;
        restTemplate.put(url, resource, SeasonDTO.class);
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }

    @ApiOperation(value = "Delete a season ", response = ResponseWrapper.class)
    @DeleteMapping("/seasons/{code}")
    public ResponseWrapper<SeasonDTO> deleteSeasonByCode(@PathVariable String code) {
        if (getSeasonByCode(code).getResponseObject() == null) {
            //Season not exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season with code: " + code + " does not exist for deletion");
        }
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + SEASONS_PATH + "/" + code + ".json?access_token=" + accessToken;
        restTemplate.delete(url);
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }

}
