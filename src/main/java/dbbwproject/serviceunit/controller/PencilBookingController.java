package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dto.*;
import dbbwproject.serviceunit.dto.response.ErrStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import dbbwproject.serviceunit.dao.FPencilBooking;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Pencil Booking Management", description = "handling pencil booking resource operations")
@RequestMapping("/resource-management/seasons/")
public class PencilBookingController extends ResourseController {
    private static final String localResourcePath = "/pencil-bookings";

    @Autowired
    protected PencilBookingController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp, localResourcePath);
    }

    @GetMapping("/trips/pencil-booking-status")
    @ApiOperation(value = "Retrieve a list of all pencil booking status", response = ResponseWrapperList.class)
    public ResponseWrapperList<PencilBookingStatus> getAllPencilBookingStatus() {
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

        FPencilBooking fPencilBooking = modelMapper.map(resource, FPencilBooking.class);
        String key = seasonCode + "_" + tripCode + "_" + resource.getPersonName();
        String errMsg = "trip with code: " + tripCode + ", season code: " + seasonCode + " and person name: " + resource.getPersonName() + " already exists in database";
        ResponseWrapper<PencilBookingDTO> res = retrieveDataAvailability(PencilBookingDTO.class, FPencilBooking.class, dbRef.child(key), errMsg);
        if (res.getStatus() == ErrStatus.DATA_AVAILABLE) {
            res.setStatus(ErrStatus.ERROR);
            return res;
        }
        DatabaseReference dbr = dbRef.child(key);
        return insertDataToDB(PencilBookingDTO.class, fPencilBooking, dbr);
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Retrieve pencil booking by person name", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> getPencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        ResponseWrapper<FPencilBooking> res = retrieveData(FPencilBooking.class, dbRef.child(seasonCode + "_" + tripCode + "_" + personName));
        if (res.getResponseObject() == null) {
            return new ResponseWrapper<>(res.getStatus(), null, res.getErrorMsg());
        }
        return new ResponseWrapper<>(res.getStatus(), modelMapper.map(res.getResponseObject(), PencilBookingDTO.class));
    }

    @GetMapping("{seasonCode}/trips/{tripCode}/pencil-bookings")
    @ApiOperation(value = "Retrieve a list of all pencil bookings belong to a trip", response = ResponseWrapperList.class)
    public ResponseWrapperList<PencilBookingDTO> getAllPencilBoookingsForTrip(@PathVariable String seasonCode, @PathVariable String tripCode) {
        Query query = dbRef.orderByChild("tripSeasonIndex").equalTo(seasonCode + "_" + tripCode);
        ResponseWrapperList<FPencilBooking> result = retrieveDataList(FPencilBooking.class, query);
        if (result.getStatus() == ErrStatus.ERROR) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, result.getErrorMsg());
        }
        java.lang.reflect.Type penBookingDTOListType = new TypeToken<List<PencilBookingDTO>>() {
        }.getType();
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, modelMapper.map(result.getResponseObjectList(), penBookingDTOListType));
    }


    @PutMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Modify existing trip by person name", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> modifyPncilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName, @RequestBody PencilBookingDTO resource) {
        if (!seasonCode.equals(resource.getSeasonCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season code: " + resource.getSeasonCode() + " and code in url: " + seasonCode + " does not match");
        }
        if (!tripCode.equals(resource.getTripCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "trip code: " + resource.getTripCode() + " and code in url: " + tripCode + " does not match");
        }
        if (!personName.equals(resource.getPersonName())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "person name: " + resource.getPersonName() + " and code in url: " + personName + " does not match");
        }

        FPencilBooking fPencilBooking = modelMapper.map(resource, FPencilBooking.class);
        String key = seasonCode + "_" + tripCode + "_" + personName;
        String errMsg = "trip with code: " + tripCode + ", season code: " + seasonCode + " and person name: " + personName + " does not exist in database";
        ResponseWrapper<PencilBookingDTO> res = retrieveDataAvailability(PencilBookingDTO.class, FPencilBooking.class, dbRef.child(key), errMsg);
        if (res.getStatus() == ErrStatus.DATA_UNAVAILABLE) {
            res.setStatus(ErrStatus.ERROR);
            return res;
        }
        DatabaseReference dbr = dbRef.child(key);
        return insertDataToDB(PencilBookingDTO.class, fPencilBooking, dbr);
    }


    @DeleteMapping("{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}")
    @ApiOperation(value = "Delete a pencil booking", response = ResponseWrapper.class)
    public ResponseWrapper<PencilBookingDTO> deletePencilBookingByPersonName(@PathVariable String seasonCode, @PathVariable String tripCode, @PathVariable String personName) {
        String key = seasonCode + "_" + tripCode + "_" + personName;
        String errMsg = "trip with code: " + tripCode + ", season code: " + seasonCode + " and person name: " + personName + " does not exist in database for deletion";
        ResponseWrapper<PencilBookingDTO> res = retrieveDataAvailability(PencilBookingDTO.class, FPencilBooking.class, dbRef.child(key), errMsg);
        if (res.getStatus() == ErrStatus.DATA_UNAVAILABLE) {
            res.setStatus(ErrStatus.ERROR);
            return res;
        }
        DatabaseReference dbr = dbRef.child(key);
        return deleteDataFromDB(PencilBookingDTO.class, dbr);
    }
}
