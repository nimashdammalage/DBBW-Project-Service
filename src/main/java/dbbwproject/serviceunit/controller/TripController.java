package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.TripDTO;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/resource-management/seasons/")
@Api(value = "Trip Management", description = "handling trip resource operations")
public class TripController extends ResourseController {

    @Autowired
    public TripController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
    }

    @ApiOperation(value = "Retrieve a list of all trip status", response = ResponseEntity.class)
    @GetMapping("trips/trip-status")
    public ResponseEntity<List<TripStatus>> getAllTripStatus() {
        return new ResponseEntity<>(Arrays.asList(TripStatus.values()), HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/booked-seat-numbers")
    @ApiOperation(value = "Retrieve a list of all pencil booking status", response = ResponseEntity.class)
    public ResponseEntity<List<Integer>> getBookedSeatNumbersForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        String key = seasonCode + "_" + tripCode;
        String seasonNotExist = "season with code: " + seasonCode + " does not exist in database";
        String tripNotExist = "trip with code " + tripCode + " and season: " + seasonCode + " does not exist in database";
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), seasonNotExist);
        ValidateResource.validateDataAvailability(FTrip.class, true, dbRef.child(FTrip.key).child(key), tripNotExist);

        Query query = dbRef.child(FPencilBooking.key).orderByChild("tripSeasonIndex").equalTo(key);
        ResponseEntity<List<FPencilBooking>> result = DBHandle.retrieveDataList(FPencilBooking.class, query);
        if (result.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(result.getStatusCode());
        }

        //collect data
        List<String> regNumberStrings = result.getBody().stream().map(pb -> pb.getRegistrationNumbers()).collect(Collectors.toList());
        List<Integer> regNumList = new ArrayList<>();
        for (String regNumberString : regNumberStrings) {
            if (regNumberStrings != null && !regNumberString.isEmpty())
                regNumList.addAll(Arrays.asList(regNumberString.split(",")).stream().filter(s -> s != null && !s.trim().isEmpty()).map(n -> Integer.parseInt(n)).collect(Collectors.toList()));
        }
        return new ResponseEntity<>(regNumList, HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips")
    @ApiOperation(value = "Retrieve a list of all trips belong to a season", response = ResponseEntity.class)
    public ResponseEntity<List<TripDTO>> getAllTripsForSeason(@PathVariable String seasonCode) {
        Query query = dbRef.child(FTrip.key).orderByChild("seasonCode").equalTo(seasonCode);
        ResponseEntity<List<FTrip>> fseasons = DBHandle.retrieveDataList(FTrip.class, query);
        if (fseasons.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fseasons.getStatusCode());
        }
        java.lang.reflect.Type tripDTOListType = new TypeToken<List<TripDTO>>() {
        }.getType();
        if (fseasons.getBody() == null || fseasons.getBody().isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<TripDTO> map = modelMapper.map(fseasons.getBody(), tripDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Retrieve trip by code", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> getTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        String key = seasonCode + "_" + tripCode;
        ResponseEntity<FTrip> res = DBHandle.retrieveData(FTrip.class, dbRef.child(FTrip.key).child(key));
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(modelMapper.map(res.getBody(), TripDTO.class), HttpStatus.OK);
    }

    @PutMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Modify existing trip by code", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> modifyTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode, @Valid @RequestBody TripDTO resource) {
        String key = seasonCode + "_" + tripCode;
        FTrip fTrip = modelMapper.map(resource, FTrip.class);
        String seasonNotExist = "season with code: " + seasonCode + " does not exist in database";
        String tripNotExist = "trip with code " + tripCode + " and season: " + seasonCode + " does not exist in database";
        String completedSeasonFound = "can not modify a trip in a completed season. season code : " + seasonCode;
        ValidateResource.validateArgument(!tripCode.equals(resource.getCode()), "trip code: " + resource.getCode() + " and code in url: " + tripCode + " does not match");
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), "season code: " + resource.getSeasonCode() + " and code in url: " + seasonCode + " does not match");
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), seasonNotExist);
        ValidateResource.validateDataAvailability(FTrip.class, true, dbRef.child(FTrip.key).child(key), tripNotExist);
        ValidateResource.validateDataAvailability(FSeason.class, false, dbRef.child(FSeason.key).child(seasonCode).orderByChild(FSeason.STATUS).equalTo(SeasonStatus.COMPLETED.toString()), completedSeasonFound);

        DatabaseReference dbr = dbRef.child(FTrip.key).child(key);
        return DBHandle.insertDataToDB(fTrip, dbr);
    }

    @PostMapping("{seasonCode}/trips")
    @ApiOperation(value = "Create a trip", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> createNewTrip(@PathVariable String seasonCode, @Valid @RequestBody TripDTO resource) {
        String key = seasonCode + "_" + resource.getCode();
        FTrip fTrip = modelMapper.map(resource, FTrip.class);
        String seasonNotExist = "season with code: " + seasonCode + " does not exist in database";
        String tripAlreadyExists = "trip with code: " + resource.getCode() + " and season: " + seasonCode + " already exists in database";
        String completedSeasonFound = "can not insert a trip into a completed season. season code : " + seasonCode;
        ValidateResource.validateArgument(resource.getCode() == null || resource.getCode().isEmpty(), "trip code can not be empty or null");
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), "season code in resource does not match with path variable");
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), seasonNotExist);
        ValidateResource.validateDataAvailability(FTrip.class, false, dbRef.child(FTrip.key).child(key), tripAlreadyExists);
        ValidateResource.validateDataComparison(String.class, false, dbRef.child(FSeason.key).child(seasonCode).child("status"), SeasonStatus.COMPLETED.toString(), completedSeasonFound);

        DatabaseReference dbr = dbRef.child(FTrip.key).child(key);
        return DBHandle.insertDataToDB(fTrip, dbr);
    }

    @DeleteMapping("{seasonCode}/trips/{tripCode}")
    @ApiOperation(value = "Delete a trip", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> deleteTripByCode(@PathVariable String seasonCode, @PathVariable String tripCode) {
        String key = seasonCode + "_" + tripCode;
        String tripNotExistForDeletion = "trip with code: " + tripCode + " and season code: " + seasonCode + " does not exist in database for deletion";
        String linkedPenBookingExists = "pencil bookings with trip code: " + tripCode + " and season code: " + seasonCode + " found. season can not be deleted";
        ValidateResource.validateDataAvailability(FTrip.class, true, dbRef.child(FTrip.key).child(key), tripNotExistForDeletion);
        ValidateResource.validateDataAvailability(FPencilBooking.class, false, dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.TRIP_SEASON_INDEX).equalTo(key), linkedPenBookingExists);

        DatabaseReference dbr = dbRef.child(FTrip.key).child(key);
        return DBHandle.deleteDataFromDB(dbr);
    }

}
