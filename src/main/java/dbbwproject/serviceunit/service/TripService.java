package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.TripDTO;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TripService extends AbstractService {
    private final java.lang.reflect.Type tripDTOListType;

    @Autowired
    public TripService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
        tripDTOListType = new TypeToken<List<TripDTO>>() {
        }.getType();
    }

    public ResponseEntity<List<Integer>> getBookedSeatNumbersForTrip(String seasonCode, String tripCode) {
        String key = seasonCode + "_" + tripCode;
        ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(key), String.format(tripNotExist, tripCode, seasonCode));

        Query query = dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.SEASON_TRIP_INDEX).equalTo(key);
        ResponseEntity<List<FPencilBooking>> result = DBHandle.retrieveDataList(FPencilBooking.class, query);
        if (result.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(result.getStatusCode());
        }

        //collect data
        if (CollectionUtils.isEmpty(result.getBody())) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        List<String> regNumberStrings = result.getBody().stream().map(FPencilBooking::getRegistrationNumbers).collect(Collectors.toList());
        List<Integer> regNumList = new ArrayList<>();
        for (String regNumberString : regNumberStrings) {
            if (!CollectionUtils.isEmpty(regNumberStrings))
                regNumList.addAll(Arrays.stream(regNumberString.split(",")).filter(s -> s != null && !s.trim().isEmpty()).map(Integer::parseInt).collect(Collectors.toList()));
        }
        return new ResponseEntity<>(regNumList, HttpStatus.OK);
    }

    public ResponseEntity<List<TripDTO>> getAllTripsForSeason(String seasonCode, String lastTripCode, int size) {
        Query query;
        if (StringUtils.isBlank(lastTripCode)) {
            query = dbRef.child(FTrip.key).orderByChild("seasonCode").equalTo(seasonCode).limitToFirst(size);
        } else {
            String lastTripKey = seasonCode + "_" + lastTripCode;
            query = dbRef.child(FTrip.key).orderByKey().startAt(lastTripKey).endAt(seasonCode + "\uf8ff").limitToFirst(size);
        }
        ResponseEntity<List<FTrip>> fseasons = DBHandle.retrieveDataList(FTrip.class, query);
        if (fseasons.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fseasons.getStatusCode());
        }

        if (CollectionUtils.isEmpty(fseasons.getBody())) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<TripDTO> map = modelMapper.map(fseasons.getBody(), tripDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<TripDTO> getTripByCode(String seasonCode, String tripCode) {
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

    public ResponseEntity modifyTripByCode(String seasonCode, String tripCode, TripDTO resource) {
        ValidateResource.validateArgument(!tripCode.equals(resource.getCode()), String.format(tripCodeUrlNotMatch, resource.getCode(), tripCode));
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), String.format(seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        String key = seasonCode + "_" + tripCode;
        FSeason fSeason = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        ValidateResource.validateArgument(fSeason.getStatus() == SeasonStatus.COMPLETED, String.format(completedSeasonFound, seasonCode));
        ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(key), String.format(tripNotExist, tripCode, seasonCode));

        FTrip fTrip = modelMapper.map(resource, FTrip.class);
        DatabaseReference dbr = dbRef.child(FTrip.key).child(key);
        return DBHandle.insertDataToDB(fTrip, dbr);
    }

    public ResponseEntity createNewTrip(String seasonCode, TripDTO resource) {
        ValidateResource.validateArgument(StringUtils.isBlank(resource.getCode()), "trip code can not be empty or null");
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), String.format(seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        String key = seasonCode + "_" + resource.getCode();
        FSeason fSeason = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        ValidateResource.validateArgument(fSeason.getStatus() == SeasonStatus.COMPLETED, String.format(completedSeasonFound, seasonCode));
        ValidateResource.validateDataAvaiAndReturn(FTrip.class, false, dbRef.child(FTrip.key).child(key), String.format(tripAlreadyExists, resource.getCode(), seasonCode));

        FTrip fTrip = modelMapper.map(resource, FTrip.class);
        DatabaseReference dbr = dbRef.child(FTrip.key).child(key);
        return DBHandle.insertDataToDB(fTrip, dbr);
    }

    public ResponseEntity deleteTripByCode(String seasonCode, String tripCode) {
        String key = seasonCode + "_" + tripCode;

        ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(key), String.format(tripNotExist, tripCode, seasonCode));
        ValidateResource.validateDataAvaiAndReturn(FPencilBooking.class, false, dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.SEASON_TRIP_INDEX).equalTo(key), String.format(linkedPenBookingExists, tripCode, seasonCode));

        DatabaseReference dbr = dbRef.child(FTrip.key).child(key);
        return DBHandle.deleteDataFromDB(dbr);
    }


}
