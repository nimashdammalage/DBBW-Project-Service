package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.BookingDTO;
import dbbwproject.serviceunit.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Booking Management")
@RequestMapping("/resource-management/seasons/")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
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

}
