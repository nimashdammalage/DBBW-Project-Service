package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FBooking;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import dbbwproject.serviceunit.settings.Settings;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.stream.Collectors;

public class PencilBookingService extends AbstractService {
    private java.lang.reflect.Type penBookingDTOListType;

    @Autowired
    public PencilBookingService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
        penBookingDTOListType = new TypeToken<List<PencilBookingDTO>>() {
        }.getType();
    }

    public ResponseEntity createNewPencilBooking(String seasonCode, String tripCode, PencilBookingDTO resource) {
        String key = seasonCode + "_" + tripCode + "_" + resource.getPersonName();
        String tripKey = seasonCode + "_" + tripCode;

        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), String.format(seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        ValidateResource.validateArgument(!tripCode.equals(resource.getTripCode()), String.format(tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        FSeason fSeason = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        FTrip fTrip = ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(tripKey), String.format(tripNotExist, tripCode, seasonCode));
        ValidateResource.validateDataAvaiAndReturn(FPencilBooking.class, false, dbRef.child(FPencilBooking.key).child(key), String.format(penBkAlreadyExist, seasonCode, tripCode, resource.getPersonName()));
        ValidateResource.validateArgument(fTrip.getTripStatus() == TripStatus.COMPLETED, String.format(completeTRipFound, tripCode, seasonCode));
        validateRegNumSequence(resource.getRegistrationNumbers(), seasonCode, tripCode, resource.getPersonName(), fTrip.getPassengerCount());
        validateMeetingpDateMaxExceed(seasonCode, tripCode, resource.getMeetUpDate());

        FPencilBooking fPencilBooking = modelMapper.map(resource, FPencilBooking.class);
        fPencilBooking.setCreatedTimestamp(new Date().getTime() * -1);
        DatabaseReference dbr = dbRef.child(FPencilBooking.key).child(key);
        return DBHandle.insertDataToDB(fPencilBooking, dbr);
    }

    public ResponseEntity<PencilBookingDTO> getPencilBookingByPersonName(String seasonCode, String tripCode, String personName) {
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

    public ResponseEntity<List<PencilBookingDTO>> getAllPencilBookingsForTrip(String seasonCode, String tripCode, String lastPersonName, int size) {
        Query query;
        if (StringUtils.isBlank(lastPersonName)) {
            query = dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.SEASON_TRIP_INDEX).equalTo(seasonCode + "_" + tripCode).limitToFirst(size);
        } else {
            String lastPPKey = seasonCode + "_" + tripCode + "_" + lastPersonName;
            query = dbRef.child(FPencilBooking.key).orderByKey().startAt(lastPPKey).endAt(seasonCode + "_" + tripCode + "\uf8ff").limitToFirst(size);
        }
        ResponseEntity<List<FPencilBooking>> fPenBks = DBHandle.retrieveDataList(FPencilBooking.class, query);
        if (fPenBks.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fPenBks.getStatusCode());
        }
        if (CollectionUtils.isEmpty(fPenBks.getBody())) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<PencilBookingDTO> map = modelMapper.map(fPenBks.getBody(), penBookingDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity modifyPencilBookingByPersonName(String seasonCode, String tripCode, String personName, PencilBookingDTO resource) {
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), String.format(seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        ValidateResource.validateArgument(!tripCode.equals(resource.getTripCode()), String.format(tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        ValidateResource.validateArgument(!personName.equals(resource.getPersonName()), String.format(personNameUrlNotMatch, resource.getPersonName(), personName));

        String key = seasonCode + "_" + tripCode + "_" + personName;
        String tripKey = seasonCode + "_" + tripCode;
        FSeason fSeason = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        FTrip fTrip = ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(tripKey), String.format(tripNotExist, tripCode, seasonCode));
        FPencilBooking fPencilBookingOld = ValidateResource.validateDataAvaiAndReturn(FPencilBooking.class, true, dbRef.child(FPencilBooking.key).child(key), String.format(penBkNotExist, seasonCode, tripCode, personName));
        ValidateResource.validateArgument(fTrip.getTripStatus() == TripStatus.COMPLETED, String.format(completeTRipFound, tripCode, seasonCode));
        validateRegNumSequence(resource.getRegistrationNumbers(), seasonCode, tripCode, personName, fTrip.getPassengerCount());
        validateMeetingpDateMaxExceed(seasonCode, tripCode, resource.getMeetUpDate());

        FPencilBooking fPencilBooking = modelMapper.map(resource, FPencilBooking.class);
        fPencilBooking.setModifiedTimestamp(new Date().getTime() * -1);
        fPencilBooking.setCreatedTimestamp(fPencilBookingOld.getCreatedTimestamp());
        DatabaseReference dbr = dbRef.child(FPencilBooking.key).child(key);
        return DBHandle.insertDataToDB(fPencilBooking, dbr);
    }

    private void validateRegNumSequence(String registrationNumbers, String seasonCode, String tripCode, String personName, int passengerCount) {
        List<Integer> dataSequence;
        try {
            dataSequence = Arrays.stream(registrationNumbers.split(","))
                    .map(String::trim)
                    .filter(s -> !StringUtils.isBlank(s))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResourceAccessException(invalidRegNumList);
        }

        //has duplicates?
        Set<Integer> dataSeqSet = new HashSet<>(dataSequence);
        if (dataSeqSet.size() != dataSequence.size()) {
            throw new ResourceAccessException(duplicateRegNumbers);
        }

        //exceeds passengerCountMax value?
        OptionalInt max = dataSequence.stream().mapToInt(v -> v).max();
        if (max.isPresent()) {
            if (max.getAsInt() > passengerCount)
                throw new ResourceAccessException(String.format(RegNumberExceedMaxCount, Integer.toString(max.getAsInt()), Integer.toString(passengerCount)));
        }

        //contains zero?
        OptionalInt min = dataSequence.stream().mapToInt(v -> v).min();
        if (min.isPresent() && min.getAsInt() == 0) {
            throw new ResourceAccessException(RegNumberConZero);
        }

        //retrieve registration numbers of other pencil bookings
        String key = seasonCode + "_" + tripCode;
        Query query = dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.SEASON_TRIP_INDEX).equalTo(key);
        ResponseEntity<List<FPencilBooking>> result = DBHandle.retrieveDataList(FPencilBooking.class, query);
        if (result.getStatusCode() != HttpStatus.OK) {
            throw new ResourceAccessException("Internal Server Error. validateRegNumSequence method");
        }

        //if modification flow, remove already saved pencil booking from consideration
        List<FPencilBooking> filteredPbs =
                Optional.ofNullable(result.getBody())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .filter(pb -> !(personName.equals(pb.getPersonName()) && tripCode.equals(pb.getTripCode()) && seasonCode.equals(pb.getSeasonCode())))
                        .collect(Collectors.toList());

        //collect data and extract existing booked seats
        Set<Integer> regNumSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(filteredPbs)) {
            List<String> regNumberStrings = filteredPbs.stream().map(FPencilBooking::getRegistrationNumbers).collect(Collectors.toList());
            for (String regNumberString : regNumberStrings) {
                if (!CollectionUtils.isEmpty(regNumberStrings))
                    regNumSet.addAll(Arrays.stream(regNumberString.split(",")).filter(s -> s != null && !s.trim().isEmpty()).map(Integer::parseInt).collect(Collectors.toList()));
            }
        }

        for (Integer regNum : dataSequence) {
            if (regNumSet.contains(regNum))
                throw new ResourceAccessException(String.format(alreadyAssignedRegNumber, Integer.toString(regNum)));
        }
    }

    public ResponseEntity deletePencilBookingByPersonName(String seasonCode, String tripCode, String personName) {
        String key = seasonCode + "_" + tripCode + "_" + personName;
        String seasonTripPNameIndex = seasonCode + tripCode + personName;
        ValidateResource.validateDataAvaiAndReturn(FPencilBooking.class, true, dbRef.child(FPencilBooking.key).child(key), String.format(penBkNotExist, seasonCode, tripCode, personName));
        ValidateResource.validateDataAvaiAndReturn(FBooking.class, false, dbRef.child(FBooking.key).orderByChild(FBooking.SEASON_TRIP_PNAME_INDEX).equalTo(seasonTripPNameIndex), String.format(linkedBookingExists, tripCode, seasonCode, personName));

        DatabaseReference dbr = dbRef.child(FPencilBooking.key).child(key);
        return DBHandle.deleteDataFromDB(dbr);
    }


    private void validateMeetingpDateMaxExceed(String seasonCode, String tripCode, String resourceMeetUpdate) {
        if (!Settings.getInstance().getData().isMaxPBkCustomersPerDayEnable()) return;
        Query query = dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.SEASON_TRIP_INDEX).equalTo(seasonCode + "_" + tripCode);
        ResponseEntity<List<FPencilBooking>> pbks = DBHandle.retrieveDataList(FPencilBooking.class, query);
        if (pbks.getStatusCode() != HttpStatus.OK || CollectionUtils.isEmpty(pbks.getBody())) return;

        List<PencilBookingDTO> mapDTO = modelMapper.map(pbks.getBody(), penBookingDTOListType);

        long sameMeetupCount = mapDTO.stream().filter(pbk -> pbk.getMeetUpDate().equals(resourceMeetUpdate)).count();
        if (sameMeetupCount >= Settings.getInstance().getData().getMaxPBkCustomersPerDay()) {
            throw new ResourceAccessException(String.format(maxMeetCountReached, Long.toString(sameMeetupCount)));
        }
    }

    public ResponseEntity<Boolean> isPencilBookingByPersonNameExist(String seasonCode, String tripCode, String personName) {
        ResponseEntity<PencilBookingDTO> pbByName = getPencilBookingByPersonName(seasonCode, tripCode, personName);
        if (pbByName.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(pbByName.getStatusCode());
        }
        if (pbByName.getBody() != null) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
