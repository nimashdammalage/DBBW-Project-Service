package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.*;
import dbbwproject.serviceunit.service.PencilBookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Pencil Booking Management")
@RequestMapping("/resource-management/seasons/")
public class PencilBookingController {
    private final PencilBookingService pencilBookingService;

    @Autowired
    public PencilBookingController(PencilBookingService pencilBookingService) {
        this.pencilBookingService = pencilBookingService;
    }

    @GetMapping("/trips/pencil-booking-status")
    @ApiOperation(value = "Retrieve a list of all pencil booking status", response = ResponseEntity.class)
    public ResponseEntity<List<DropDownDto>> getAllPencilBookingStatus() {
        List<DropDownDto> collect = Arrays.stream(PencilBookingStatus.values()).map(p -> new DropDownDto(p, p.toString())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Create a pencil booking", response = ResponseEntity.class)
    public ResponseEntity createNewPencilBooking(@PathVariable String seasonCode, @PathVariable String tripCode, @Valid @RequestBody PencilBookingDto resource) {
        return pencilBookingService.createNewPencilBooking(seasonCode, tripCode, resource);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Retrieve pencil booking by person name", response = ResponseEntity.class)
    public ResponseEntity<PencilBookingDto> getPencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        return pencilBookingService.getPencilBookingByPersonName(seasonCode, tripCode, personName);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}/exist")
    @ApiOperation(value = "Retrieve pencil booking by person name", response = ResponseEntity.class)
    public ResponseEntity<Boolean> isPencilBookingByPersonNameExist(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        return pencilBookingService.isPencilBookingByPersonNameExist(seasonCode, tripCode, personName);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Retrieve a list of all pencil bookings belong to a trip", response = ResponseEntity.class)
    public ResponseEntity<List<PencilBookingDto>> getAllPencilBookingsForTrip(@PathVariable String seasonCode, @PathVariable String tripCode, @RequestParam(name = "fIndex", required = false, defaultValue = "0") int fIndex, @RequestParam("size") int size) {
        return pencilBookingService.getAllPencilBookingsForTrip(seasonCode, tripCode, fIndex, size);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-booking-code")
    @ApiOperation(value = "Retrieve a list of all pencil booking codes belong to a trip", response = ResponseEntity.class)
    public ResponseEntity<List<DropDownDto>> getAllPencilBookingCodesForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        return pencilBookingService.getAllPencilBookingsForTripDropDown(seasonCode, tripCode);
    }

    @PutMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Modify existing trip by person name", response = ResponseEntity.class)
    public ResponseEntity modifyPencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName, @RequestBody PencilBookingDto resource) {
        return pencilBookingService.modifyPencilBookingByPersonName(seasonCode, tripCode, personName, resource);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Delete a pencil booking", response = ResponseEntity.class)
    public ResponseEntity deletePencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        return pencilBookingService.deletePencilBookingByPersonName(seasonCode, tripCode, personName);
    }

}
