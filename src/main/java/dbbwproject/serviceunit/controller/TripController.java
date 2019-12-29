package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.DropDownDTO;
import dbbwproject.serviceunit.dto.TripDTO;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.service.TripService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resource-management/seasons/")
@Api(value = "Trip Management")
public class TripController {
    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @ApiOperation(value = "Retrieve a list of all trip status for drop down", response = ResponseEntity.class)
    @GetMapping("trips/trip-status")
    public ResponseEntity<List<DropDownDTO>> getAllTripStatus() {
        List<DropDownDTO> collect = Arrays.stream(TripStatus.values()).map(t -> new DropDownDTO(t, t.toString())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/booked-seat-numbers")
    @ApiOperation(value = "Retrieve a list of all booked seats", response = ResponseEntity.class)
    public ResponseEntity<List<Integer>> getBookedSeatNumbersForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.getBookedSeatNumbersForTrip(seasonCode, tripCode);
    }

    @GetMapping("{seasonCode}/trips")
    @ApiOperation(value = "Retrieve a list of all trips belong to a season", response = ResponseEntity.class)
    public ResponseEntity<List<TripDTO>> getAllTripsForSeason(@PathVariable String seasonCode, @RequestParam(name = "lastTripCode", required = false, defaultValue = "") String lastTripCode, @RequestParam("size") int size) {
        return tripService.getAllTripsForSeason(seasonCode, lastTripCode, size);
    }

    @GetMapping("{seasonCode}/trip-code")
    @ApiOperation(value = "Retrieve a list of all trip codes belong to a season", response = ResponseEntity.class)
    public ResponseEntity<List<DropDownDTO>> getAllTripCodesForSeason(@PathVariable String seasonCode, @RequestParam(name = "lastTripCode", required = false, defaultValue = "") String lastTripCode, @RequestParam("size") int size) {
        ResponseEntity<List<TripDTO>> result = tripService.getAllTripsForSeason(seasonCode, lastTripCode, size);
        if (result.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(result.getStatusCode());
        }
        if (CollectionUtils.isEmpty(result.getBody())) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<DropDownDTO> collect = result.getBody().stream().map(t -> new DropDownDTO(t.getCode(), t.getCode())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> getTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.getTripByCode(seasonCode, tripCode);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/exist")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseEntity.class)
    public ResponseEntity<Boolean> isTripByCodeExist(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.isTripByCodeExist(seasonCode, tripCode);
    }

    @PutMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Modify existing trip by code", response = ResponseEntity.class)
    public ResponseEntity modifyTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode, @Valid @RequestBody TripDTO resource) {
        return tripService.modifyTripByCode(seasonCode, tripCode, resource);
    }

    @PostMapping("{seasonCode}/trips")
    @ApiOperation(value = "Create a trip", response = ResponseEntity.class)
    public ResponseEntity createNewTrip(@PathVariable String seasonCode, @Valid @RequestBody TripDTO resource) {
        return tripService.createNewTrip(seasonCode, resource);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Delete a trip", response = ResponseEntity.class)
    public ResponseEntity deleteTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.deleteTripByCode(seasonCode, tripCode);
    }

}
