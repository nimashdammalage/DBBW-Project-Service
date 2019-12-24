package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.TripDTO;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.service.TripService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/resource-management/seasons/")
@Api(value = "Trip Management")
public class TripController {
    private TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @ApiOperation(value = "Retrieve a list of all trip status", response = ResponseEntity.class)
    @GetMapping("trips/trip-status")
    public ResponseEntity<Map<TripStatus, TripStatus>> getAllTripStatus() {
        Map<TripStatus, TripStatus> resMap = new HashMap<>();
        Arrays.stream(TripStatus.values()).forEach(t -> resMap.put(t, t));
        return new ResponseEntity<>(resMap, HttpStatus.OK);
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
    @ApiOperation(value = "Retrieve a list of all trips belong to a season", response = ResponseEntity.class)
    public ResponseEntity<Map<String, String>> getAllTripCodesForSeason(@PathVariable String seasonCode, @RequestParam(name = "lastTripCode", required = false, defaultValue = "") String lastTripCode, @RequestParam("size") int size) {
        ResponseEntity<List<TripDTO>> result = tripService.getAllTripsForSeason(seasonCode, lastTripCode, size);
        if (result.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(result.getStatusCode());
        }
        Map<String, String> map = new HashMap<>();
        result.getBody().stream().forEach(s -> map.put(s.getCode(), s.getCode()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> getTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.getTripByCode(seasonCode, tripCode);
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
