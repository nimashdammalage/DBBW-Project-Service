package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.*;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import dbbwproject.serviceunit.settings.Settings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Pencil Booking Management", description = "handling pencil booking resource operations")
@RequestMapping("/resource-management/seasons/")
public class PencilBookingController extends ResourseController {

    @Autowired
    public PencilBookingController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
    }

    @GetMapping("/trips/pencil-booking-status")
    @ApiOperation(value = "Retrieve a list of all pencil booking status", response = ResponseEntity.class)
    public ResponseEntity<List<PencilBookingStatus>> getAllPencilBookingStatus() {
        return new ResponseEntity<>(Arrays.asList(PencilBookingStatus.values()), HttpStatus.OK);
    }

    @PostMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Create a pencil booking", response = ResponseEntity.class)
    public ResponseEntity<PencilBookingDTO> createNewPencilBooking(@PathVariable String seasonCode, @PathVariable String tripCode, @Valid @RequestBody PencilBookingDTO resource) {
        String key = seasonCode + "_" + tripCode + "_" + resource.getPersonName();
        String tripKey = seasonCode + "_" + tripCode;
        FPencilBooking fPencilBooking = modelMapper.map(resource, FPencilBooking.class);
        String seasonNotExist = "season with code: " + seasonCode + " does not exist in database";
        String tripNotExist = "trip with code " + tripCode + " and season code: " + seasonCode + " does not exist in database";
        String penBkExist = "trip with code: " + tripCode + ", season code: " + seasonCode + " and person name: " + resource.getPersonName() + " already exists in database";
        String completeTRipFound = "can not insert a pencil booking into a completed trip. trip code: " + tripCode + ", season code: " + seasonCode;
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), "season code in resource does not match with path variable");
        ValidateResource.validateArgument(!tripCode.equals(resource.getTripCode()), "trip code in resource does not match with path variable");
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), seasonNotExist);
        ValidateResource.validateDataAvailability(FTrip.class, true, dbRef.child(FTrip.key).child(tripKey), tripNotExist);
        ValidateResource.validateDataAvailability(FPencilBooking.class, false, dbRef.child(FPencilBooking.key).child(key), penBkExist);
        ValidateResource.validateDataComparison(String.class, false, dbRef.child(FTrip.key).child(tripKey).child(FTrip.TRIP_STATUS), TripStatus.COMPLETED.toString(), completeTRipFound);
        validateMeeupDateExceed(seasonCode, tripCode, resource.getMeetUpDate());

        DatabaseReference dbr = dbRef.child(FPencilBooking.key).child(key);
        return DBHandle.insertDataToDB(fPencilBooking, dbr);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Retrieve pencil booking by person name", response = ResponseEntity.class)
    public ResponseEntity<PencilBookingDTO> getPencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        String key = seasonCode + "_" + tripCode + "_" + personName;
        ResponseEntity<FPencilBooking> res = DBHandle.retrieveData(FPencilBooking.class, dbRef.child(FPencilBooking.key).child(key));
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(modelMapper.map(res.getBody(), PencilBookingDTO.class), HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Retrieve a list of all pencil bookings belong to a trip", response = ResponseEntity.class)
    public ResponseEntity<List<PencilBookingDTO>> getAllPencilBookingsForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        Query query = dbRef.child(FPencilBooking.key).orderByChild("tripSeasonIndex").equalTo(seasonCode + "_" + tripCode);
        ResponseEntity<List<FPencilBooking>> fPenBks = DBHandle.retrieveDataList(FPencilBooking.class, query);
        if (fPenBks.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fPenBks.getStatusCode());
        }
        java.lang.reflect.Type penBookingDTOListType = new TypeToken<List<PencilBookingDTO>>() {
        }.getType();
        if (fPenBks.getBody() == null || fPenBks.getBody().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        List<PencilBookingDTO> map = modelMapper.map(fPenBks.getBody(), penBookingDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Modify existing trip by person name", response = ResponseEntity.class)
    public ResponseEntity<PencilBookingDTO> modifyPncilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName, @RequestBody PencilBookingDTO resource) {
        String key = seasonCode + "_" + tripCode + "_" + personName;
        String tripKey = seasonCode + "_" + tripCode;
        FPencilBooking fPencilBooking = modelMapper.map(resource, FPencilBooking.class);
        String seasonNotExist = "season with code: " + seasonCode + " does not exist in database";
        String tripNotExist = "trip with code " + tripCode + " and season: " + seasonCode + " does not exist in database";
        String pbNotExist = "trip with code: " + tripCode + ", season code: " + seasonCode + " and person name: " + personName + " does not exist in database";
        String completeTRipFound = "can not modify a pencil booking of a completed trip. trip code: " + tripCode + ", season code: " + seasonCode;
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), "season code: " + resource.getSeasonCode() + " and code in url: " + seasonCode + " does not match");
        ValidateResource.validateArgument(!tripCode.equals(resource.getTripCode()), "trip code: " + resource.getTripCode() + " and code in url: " + tripCode + " does not match");
        ValidateResource.validateArgument(!personName.equals(resource.getPersonName()), "person name: " + resource.getPersonName() + " and code in url: " + personName + " does not match");
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), seasonNotExist);
        ValidateResource.validateDataAvailability(FTrip.class, true, dbRef.child(FTrip.key).child(tripKey), tripNotExist);
        ValidateResource.validateDataAvailability(FPencilBooking.class, true, dbRef.child(FPencilBooking.key).child(key), pbNotExist);
        ValidateResource.validateDataComparison(String.class, false, dbRef.child(FTrip.key).child(tripKey).child(FTrip.TRIP_STATUS), TripStatus.COMPLETED.toString(), completeTRipFound);
        validateMeeupDateExceed(seasonCode, tripCode, resource.getMeetUpDate());

        DatabaseReference dbr = dbRef.child(FPencilBooking.key).child(key);
        return DBHandle.insertDataToDB(fPencilBooking, dbr);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Delete a pencil booking", response = ResponseEntity.class)
    public ResponseEntity<PencilBookingDTO> deletePencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        String key = seasonCode + "_" + tripCode + "_" + personName;
        String pbNotExistForDeletion = "trip with code: " + tripCode + ", season code: " + seasonCode + " and person name: " + personName + " does not exist in database for deletion";
        ValidateResource.validateDataAvailability(FPencilBooking.class, true, dbRef.child(FPencilBooking.key).child(key), pbNotExistForDeletion);
        //todo : once bookings created , add a validation for not deleting pencil booking
        DatabaseReference dbr = dbRef.child(FPencilBooking.key).child(key);
        return DBHandle.deleteDataFromDB(dbr);
    }

    private void validateMeeupDateExceed(String seasonCode, String tripCode, String resourceMeetUpdate) {
        if (!Settings.getInstance().getData().isMaxPBkCustomersPerDayEnable()) return;
        ResponseEntity<List<PencilBookingDTO>> pbks = getAllPencilBookingsForTrip(seasonCode, tripCode);
        if (pbks.getStatusCode() != HttpStatus.OK || pbks.getBody() == null) return;

        long sameMeetupCount = pbks.getBody().stream().filter(pbk -> pbk.getMeetUpDate().equals(resourceMeetUpdate)).count();
        if (sameMeetupCount >= Settings.getInstance().getData().getMaxPBkCustomersPerDay()) {
            throw new ResourceAccessException("date :" + resourceMeetUpdate + " is already assigned for " + sameMeetupCount + " customers. please choose another meet up date");
        }
    }
}
