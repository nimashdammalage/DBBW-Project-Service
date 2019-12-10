package dbbwproject.serviceunit.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dbbwproject.serviceunit.dto.*;
import dbbwproject.serviceunit.dto.response.ErrStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import dbbwproject.serviceunit.firebasehandler.AccessTokenGenrator;
import dbbwproject.serviceunit.firebasehandler.jsonobjects.FPencilBooking;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Pencil Booking Management", description = "handling pencil booking resource operations")
@RequestMapping("/resource-management/seasons/")
public class PencilBookingController extends ResourseContoller {
    private static final String PENCIL_BOOKING_PATH = "/pencil-bookings";

    @Autowired
    protected PencilBookingController(RestTemplate restTemplate, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(restTemplate, modelMapper, objectMapper);
    }

    @GetMapping("/trips/pencil-booking-status")
    @ApiOperation(value = "Retrieve a list of all pencil booking status", response = ResponseWrapperList.class)
    public ResponseWrapperList<PencilBookingStatus> getAllSeasonStatus() {
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, Arrays.asList(PencilBookingStatus.values()));
    }

    @PostMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Create a pencil booking", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> createNewPencilBooking(@PathVariable String seasonCode, @PathVariable String tripCode, @RequestBody PencilBookingDTO resource) {
        if (resource.getPersonName() == null || resource.getPersonName().isEmpty()) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "person name can not be empty or null");
        }
        if (!seasonCode.equals(resource.getSeasonCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season code in resource does not match with path variable");
        }
        if (!tripCode.equals(resource.getTripCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip code in resource does not match with path variable");
        }
        if (getPencilBookingByPersonName(seasonCode, tripCode, resource.getPersonName()).getResponseObject() != null) {
            //Pencil Booking already exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip with code: " + tripCode + ", season: " + seasonCode + "and person name: " + resource.getPersonName() + " already exists");
        }

        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + PENCIL_BOOKING_PATH + "/" + seasonCode + "_" + tripCode + "_" + resource.getPersonName() + ".json?access_token=" + accessToken;
        FPencilBooking fResource = modelMapper.map(resource, FPencilBooking.class);
        restTemplate.put(url, fResource, FPencilBooking.class);
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Retrieve pencil booking by person name", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> getPencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + "/" + PENCIL_BOOKING_PATH + "/" + seasonCode + "_" + tripCode + "_" + personName + ".json?access_token=" + accessToken;
        ResponseEntity<FPencilBooking> result = restTemplate.getForEntity(url, FPencilBooking.class);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "error in call firebase get method" + result.getBody().toString());
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, result.getBody() == null ? null : modelMapper.map(result.getBody(), PencilBookingDTO.class));
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Retrieve a list of all pencil bookings belong to a trip", response = ResponseWrapperList.class)
    public ResponseWrapperList<PencilBookingDTO> getAllPencilBoookingsForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + "/" + PENCIL_BOOKING_PATH + ".json?orderBy=\"tripSeasonIndex\"&equalTo=\"" + seasonCode + "_" + tripCode + "\"&" + "access_token=" + accessToken;
        Map<String, PencilBookingDTO> result = restTemplate.getForObject(url, Map.class);
        if (result == null) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, "error in getAllTrips for season");
        }
        List<FPencilBooking> deoList = objectMapper.convertValue(new ArrayList<>(result.values()), new TypeReference<List<FPencilBooking>>() {
        });
        List<PencilBookingDTO> dtoList = deoList.stream().map(d -> modelMapper.map(d, PencilBookingDTO.class)).collect(Collectors.toList());
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, dtoList, null);
    }


    @PutMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Modify existing trip by person name", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> modifyPncilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName, @RequestBody PencilBookingDTO resource) {
        if (!tripCode.equals(resource.getTripCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip code: " + resource.getPersonName() + " and code in url: " + tripCode + " does not match");
        }
        if (!seasonCode.equals(resource.getSeasonCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season code: " + resource.getSeasonCode() + " and code in url: " + seasonCode + " does not match");
        }
        if (!personName.equals(resource.getPersonName())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "person name: " + resource.getPersonName() + " and code in url: " + personName + " does not match");
        }
        if (getPencilBookingByPersonName(seasonCode, tripCode, personName).getResponseObject() == null) {
            //Pencil booking not exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "pencil booking for person: " + personName + ",trip code: " + resource.getPersonName() + " and season code: " + seasonCode + " does not exist in DB for modification");
        }

        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + PENCIL_BOOKING_PATH + "/" + seasonCode + "_" + tripCode + "_" + personName + ".json?access_token=" + accessToken;
        restTemplate.put(url, modelMapper.map(resource, FPencilBooking.class), FPencilBooking.class);
        PencilBookingDTO updatedTrip = getPencilBookingByPersonName(seasonCode, tripCode, personName).getResponseObject();
        if (updatedTrip == null) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "error in calling in firebase API");
        }
        return new ResponseWrapper<>(ErrStatus.SUCCESS, updatedTrip);
    }


    @DeleteMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Delete a pencil booking", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> deletePencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        if (getPencilBookingByPersonName(seasonCode, tripCode, personName).getResponseObject() == null) {
            //Trip not exists
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip with person name: " + personName + ", trip code: " + tripCode + " and season code: " + seasonCode + " does not exist for deletion");
        }
        String accessToken;
        try {
            accessToken = AccessTokenGenrator.getAccessToken(serviceAccountKeyPath);
        } catch (IOException e) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "unable to generate firebase access token" + e.getMessage());
        }
        String url = fireBaseDBUrl + PENCIL_BOOKING_PATH + "/" + seasonCode + "_" + tripCode + "_" + personName + ".json?access_token=" + accessToken;
        restTemplate.delete(url);
        return new ResponseWrapper<>(ErrStatus.SUCCESS, null);
    }
}
