package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.TripDTO;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.dto.response.ErrStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import dbbwproject.serviceunit.firebasehandler.AccessTokenGenrator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/resource-management/seasons")
@Api(value = "Trip Management", description = "handling trip resource operations")
public class TripController extends ResourseContoller{
    private final String TRIP_PATH = "/trips";

    @Autowired
    public TripController(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @GetMapping("/trip-status")
    @ApiOperation(value = "Retrieve a list of all trip status", response = ResponseWrapperList.class)
    public ResponseWrapperList<TripStatus> getAllTripStatus() {
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, Arrays.asList(TripStatus.values()));
    }

    @GetMapping("{seasonCode}/trips")
    @ApiOperation(value = "Retrieve a list of all trips belong to a season", response = ResponseWrapperList.class)
    public ResponseWrapperList<TripDTO> getAllTripsForSeason(@PathVariable String seasonCode) {
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + "/" + TRIP_PATH + ".json?orderBy=\"seasonCode\"&equalTo=\"" + seasonCode + "\"&" + "access_token=" + accessToken;
        Map<String, TripDTO> result = restTemplate.getForObject(url, Map.class);
        if (result == null) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, "error in getAllTrips for season");
        }
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, new ArrayList<>(result.values()), null);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseWrapper.class)
    public ResponseWrapper<TripDTO> getTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + "/" + TRIP_PATH + "/" + seasonCode + "_" + tripCode + ".json?access_token=" + accessToken;
        ResponseEntity<TripDTO> result = restTemplate.getForEntity(url, TripDTO.class);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "error in call firebase get method" + result.getBody().toString());
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, result.getBody());
    }

    @PutMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Modify existing trip by code", response = ResponseWrapper.class)
    public ResponseWrapper<TripDTO> modifyTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode, @RequestBody TripDTO resource) {
        if (!tripCode.equals(resource.getCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip code: " + resource.getCode() + "and code in url: " + tripCode + " does not match");
        }
        if (!seasonCode.equals(resource.getSeasonCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season code: " + resource.getSeasonCode() + "and code in url: " + seasonCode + " does not match");
        }
        if (getTripByCode(seasonCode, tripCode).getResponseObject() == null) {
            //Trip not exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip with code: " + resource.getCode() + " and season: " + seasonCode + " does not exist in DB for modification");
        }

        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + TRIP_PATH + "/" + seasonCode + "_" + tripCode + ".json?access_token=" + accessToken;
        restTemplate.put(url, resource, TripDTO.class);
        TripDTO updatedTrip = getTripByCode(seasonCode, tripCode).getResponseObject();
        if (updatedTrip == null) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "error in calling in firebase API");
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, updatedTrip);
    }

    @PostMapping("{seasonCode}/trips")
    @ApiOperation(value = "Create a trip", response = ResponseWrapper.class)
    public ResponseWrapper<TripDTO> createNewTrip(@PathVariable String seasonCode, @RequestBody TripDTO resource) {
        if (resource.getCode() == null || resource.getCode().isEmpty()) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip code can not be empty or null");
        }
        if (!seasonCode.equals(resource.getSeasonCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season code in resource does not match with path variable");
        }
        if (getTripByCode(seasonCode, resource.getCode()).getResponseObject() != null) {
            //Trip already exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip with code: " + resource.getCode() + " and season: " + seasonCode + " already exists");
        }

        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + TRIP_PATH + "/" + seasonCode + "_" + resource.getCode() + ".json?access_token=" + accessToken;
        restTemplate.put(url, resource, TripDTO.class);
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Delete a trip", response = ResponseWrapper.class)
    public ResponseWrapper<TripDTO> deleteTripByCode(@PathVariable String seasonCode,@PathVariable String tripCode) {
        if (getTripByCode(seasonCode, tripCode).getResponseObject() == null) {
            //Trip not exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip with code: " + tripCode + " and season: " + seasonCode + " does not exist for deletion");
        }
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + TRIP_PATH + "/" + seasonCode + "_" + tripCode + ".json?access_token=" + accessToken;
        restTemplate.delete(url);
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }

}
