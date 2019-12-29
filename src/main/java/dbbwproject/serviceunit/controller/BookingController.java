package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.*;
import dbbwproject.serviceunit.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Booking Management")
@RequestMapping("/resource-management/seasons/")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ApiOperation(value = "Retrieve a list of TypeOfService for drop down", response = ResponseEntity.class)
    @GetMapping("trips/bookings/typeofservice")
    public ResponseEntity<List<DropDownDTO>> getAllTypeOfServices() {
        List<DropDownDTO> collect = Arrays.stream(TypeOfService.values()).map(t -> new DropDownDTO(t, t.toString())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve a list of TypeOfTravelDoc for drop down", response = ResponseEntity.class)
    @GetMapping("trips/bookings/typeoftraveldoc")
    public ResponseEntity<List<DropDownDTO>> getAllTypeOfTravelDocs() {
        List<DropDownDTO> collect = Arrays.stream(TypeOfTravelDoc.values()).map(t -> new DropDownDTO(t, t.toString())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @PostMapping("{seasonCode}/trips/{tripCode}/bookings")
    @ApiOperation(value = "Create a booking", response = ResponseEntity.class)
    public ResponseEntity createNewPencilBooking(@PathVariable String seasonCode, @PathVariable String tripCode, @Valid @RequestBody BookingDTO resource) {
        return bookingService.createNewBooking(seasonCode, tripCode, resource);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/bookings/{regNumber}")
    @ApiOperation(value = "Retrieve booking by registration number", response = ResponseEntity.class)
    public ResponseEntity<BookingDTO> getBookingByRegNumber(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable int regNumber) {
        return bookingService.getBookingByRegNumber(seasonCode, tripCode, regNumber);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/bookings/{regNumber}/exist")
    @ApiOperation(value = "Retrieve booking by registration number", response = ResponseEntity.class)
    public ResponseEntity<Boolean> isBookingByRegNumberExist(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable int regNumber) {
        return bookingService.isBookingByRegNumberExist(seasonCode, tripCode, regNumber);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/bookings")
    @ApiOperation(value = "Retrieve a list of all pencil bookings belong to a trip", response = ResponseEntity.class)
    public ResponseEntity<List<BookingDTO>> getAllBookingsForTrip(@PathVariable String seasonCode, @PathVariable String tripCode, @RequestParam(name = "lastRegNumber", required = false, defaultValue = "") String lastRegNumber, @RequestParam("size") int size) {
        return bookingService.getAllBookingsForTrip(seasonCode, tripCode, lastRegNumber, size);
    }

    @PutMapping("{seasonCode}/trips/{tripCode}/bookings/{regNumber}")
    @ApiOperation(value = "Modify existing trip by person name", response = ResponseEntity.class)
    public ResponseEntity modifyBookingByRegNumber(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable int regNumber, @RequestBody BookingDTO resource) {
        return bookingService.modifyBookingByRegNumber(seasonCode, tripCode, regNumber, resource);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}/bookings/{regNumber}")
    @ApiOperation(value = "Delete a booking", response = ResponseEntity.class)
    public ResponseEntity deleteBookingByRegNumber(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable int regNumber) {
        return bookingService.deleteBookingByRegNumber(seasonCode, tripCode, regNumber);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/bookings/{regNumber}/mdyapplicationpdf")
    @ApiOperation(value = "Retrieve Mahamegha Application form(PDF) by registration number", response = ResponseEntity.class)
    public ResponseEntity<InputStreamResource> getMdyApplicationPdf(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable int regNumber) {
        return bookingService.getMdyApplicationPdf(seasonCode, tripCode, regNumber);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/bookings/{regNumber}/passportformpdf")
    @ApiOperation(value = "Retrieve Mahamegha Application form(PDF) by registration number", response = ResponseEntity.class)
    public ResponseEntity<InputStreamResource> getPassportFormPdf(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable int regNumber) {
        return bookingService.getPassportFormPdf(seasonCode, tripCode, regNumber);
    }
}
