package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.DropDownDto;
import dbbwproject.serviceunit.dto.TripDto;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.dto.datatable.DtReqDto;
import dbbwproject.serviceunit.dto.datatable.DtResponse;
import dbbwproject.serviceunit.service.TripService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resource-management/seasons/")
@Api(value = "Trip Management")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @ApiOperation(value = "Retrieve a list of all trip status for drop down", response = ResponseEntity.class)
    @GetMapping("trips/trip-status")
    public ResponseEntity<List<DropDownDto>> getAllTripStatus() {
        List<DropDownDto> collect = Arrays.stream(TripStatus.values()).map(t -> new DropDownDto(t, t.toString())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/booked-seat-numbers")
    @ApiOperation(value = "Retrieve a list of all booked seats", response = ResponseEntity.class)
    public ResponseEntity<List<Integer>> getBookedSeatNumbersForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.getBookedSeatNumbersForTrip(seasonCode, tripCode);
    }

    @GetMapping("{seasonCode}/trips")
    @ApiOperation(value = "Retrieve a list of all trips belong to a season", response = ResponseEntity.class)
    public ResponseEntity<List<TripDto>> getAllTripsForSeason(@PathVariable String seasonCode, @RequestParam(name = "fIndex", required = false, defaultValue = "0") int fIndex, @RequestParam("size") int size) {
        return tripService.getAllTripsForSeason(seasonCode, fIndex, size);
    }

    @ApiOperation(value = "Retrieve a list of trips for data table", response = ResponseEntity.class)
    @PostMapping("seasons/datatable")
    public ResponseEntity<DtResponse<TripDto>> getAllSeasonsForDT(@RequestBody DtReqDto dtReqDTO) {
        return tripService.getAllSeasonsForDT(dtReqDTO);
    }

    @GetMapping("{seasonCode}/trip-code")
    @ApiOperation(value = "Retrieve a list of all trip codes belong to a season", response = ResponseEntity.class)
    public ResponseEntity<List<DropDownDto>> getAllTripCodesForSeasonDropDown(@PathVariable String seasonCode) {
        return tripService.getAllTripCodesForSeasonDropDown(seasonCode);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseEntity.class)
    public ResponseEntity<TripDto> getTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.getTripByCode(seasonCode, tripCode);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/exist")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseEntity.class)
    public ResponseEntity<Boolean> isTripByCodeExist(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.isTripByCodeExist(seasonCode, tripCode);
    }

    @PutMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Modify existing trip by code", response = ResponseEntity.class)
    public ResponseEntity modifyTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode, @Valid @RequestBody TripDto resource) {
        return tripService.modifyTripByCode(seasonCode, tripCode, resource);
    }

    @PostMapping("{seasonCode}/trips")
    @ApiOperation(value = "Create a trip", response = ResponseEntity.class)
    public ResponseEntity createNewTrip(@PathVariable String seasonCode, @Valid @RequestBody TripDto resource) {
        return tripService.createNewTrip(seasonCode, resource);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Delete a trip", response = ResponseEntity.class)
    public ResponseEntity deleteTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return tripService.deleteTripByCode(seasonCode, tripCode);
    }

}
