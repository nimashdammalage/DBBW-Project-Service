package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FBooking;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.BookingDTO;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import dbbwproject.serviceunit.pdfhandler.application.MdyApplication;
import dbbwproject.serviceunit.pdfhandler.application.MdyApplicationHandler;
import dbbwproject.serviceunit.pdfhandler.passport.PassportForm;
import dbbwproject.serviceunit.pdfhandler.passport.PasspostFormHandler;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookingService extends AbstractService {
    private java.lang.reflect.Type bookingDTOListType;

    @Autowired
    public BookingService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
        bookingDTOListType = new TypeToken<List<BookingDTO>>() {
        }.getType();
    }

    public ResponseEntity createNewBooking(String seasonCode, String tripCode, BookingDTO resource) {
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), String.format(seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        ValidateResource.validateArgument(!tripCode.equals(resource.getTripCode()), String.format(tripCodeUrlNotMatch, resource.getTripCode(), tripCode));

        String tripKey = seasonCode + "_" + tripCode;
        String pbKey = seasonCode + "_" + tripCode + "_" + resource.getPbPersonName();
        String bookingKey = seasonCode + "_" + tripCode + "_" + resource.getRegistrationNumber();

        //data availability check
        FSeason fSeason = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        FTrip fTrip = ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(tripKey), String.format(tripNotExist, tripCode, seasonCode));
        ValidateResource.validateArgument(fTrip.getTripStatus() == TripStatus.COMPLETED, String.format(completeTRipFound, tripCode, seasonCode));
        FPencilBooking fPencilBooking = ValidateResource.validateDataAvaiAndReturn(FPencilBooking.class, true, dbRef.child(FPencilBooking.key).child(pbKey), String.format(penBkNotExist, seasonCode, tripCode, resource.getPbPersonName()));
        ValidateResource.validateDataAvaiAndReturn(FBooking.class, false, dbRef.child(FBooking.key).child(bookingKey), String.format(bookingAlreadyExist, seasonCode, tripCode, resource.getRegistrationNumber()));
        validateRegNumber(resource.getRegistrationNumber(), fPencilBooking.getPersonName(), fPencilBooking.getRegistrationNumbers());

        FBooking fBooking = modelMapper.map(resource, FBooking.class);
        fBooking.setCreatedTimestamp(new Date().getTime() * -1);
        DatabaseReference dbr = dbRef.child(FBooking.key).child(bookingKey);
        return DBHandle.insertDataToDB(fBooking, dbr);

    }

    public ResponseEntity<BookingDTO> getBookingByRegNumber(String seasonCode, String tripCode, int regNumber) {
        String key = seasonCode + "_" + tripCode + "_" + regNumber;
        ResponseEntity<FBooking> res = DBHandle.retrieveData(FBooking.class, dbRef.child(FBooking.key).child(key));
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(modelMapper.map(res.getBody(), BookingDTO.class), HttpStatus.OK);
    }

    private void validateRegNumber(int regNumber, String pbPersonName, String pbRegNumberStr) {
        List<Integer> regNumList = Arrays.stream(pbRegNumberStr.split(","))
                .map(String::trim)
                .filter(s -> !StringUtils.isBlank(s))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        ValidateResource.validateArgument(!regNumList.contains(regNumber), String.format(invalidRegNum, Integer.toString(regNumber), pbPersonName));
    }


    public ResponseEntity<List<BookingDTO>> getAllBookingsForTrip(String seasonCode, String tripCode, String lastRegNumber, int size) {
        Query query;
        if (StringUtils.isBlank(lastRegNumber)) {
            query = dbRef.child(FBooking.key).orderByChild(FBooking.SEASON_TRIP_INDEX).equalTo(seasonCode + "_" + tripCode).limitToFirst(size);
        } else {
            String lastBookingKey = seasonCode + "_" + tripCode + "_" + lastRegNumber;
            query = dbRef.child(FBooking.key).orderByKey().startAt(lastBookingKey).endAt(seasonCode + "_" + tripCode + "\uf8ff").limitToFirst(size);
        }
        ResponseEntity<List<FBooking>> fbooks = DBHandle.retrieveDataList(FBooking.class, query);
        if (fbooks.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fbooks.getStatusCode());
        }
        if (CollectionUtils.isEmpty(fbooks.getBody())) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<BookingDTO> map = modelMapper.map(fbooks.getBody(), bookingDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity modifyBookingByRegNumber(String seasonCode, String tripCode, int regNumber, BookingDTO resource) {
        ValidateResource.validateArgument(!seasonCode.equals(resource.getSeasonCode()), String.format(seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        ValidateResource.validateArgument(!tripCode.equals(resource.getTripCode()), String.format(tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        ValidateResource.validateArgument(regNumber != resource.getRegistrationNumber(), String.format(regNumberUrlNotMatch, Integer.toString(resource.getRegistrationNumber()), Integer.toString(regNumber)));

        String tripKey = seasonCode + "_" + tripCode;
        String pbKey = seasonCode + "_" + tripCode + "_" + resource.getPbPersonName();
        String bookingKey = seasonCode + "_" + tripCode + "_" + resource.getRegistrationNumber();

        FSeason fSeason = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(seasonCode), String.format(seasonNotExist, seasonCode));
        FTrip fTrip = ValidateResource.validateDataAvaiAndReturn(FTrip.class, true, dbRef.child(FTrip.key).child(tripKey), String.format(tripNotExist, tripCode, seasonCode));
        ValidateResource.validateArgument(fTrip.getTripStatus() == TripStatus.COMPLETED, String.format(completeTRipFound, tripCode, seasonCode));
        FPencilBooking fPencilBooking = ValidateResource.validateDataAvaiAndReturn(FPencilBooking.class, true, dbRef.child(FPencilBooking.key).child(pbKey), String.format(penBkNotExist, seasonCode, tripCode, resource.getPbPersonName()));
        FBooking fBookingOld = ValidateResource.validateDataAvaiAndReturn(FBooking.class, true, dbRef.child(FBooking.key).child(bookingKey), String.format(bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));
        validateRegNumber(resource.getRegistrationNumber(), fPencilBooking.getPersonName(), fPencilBooking.getRegistrationNumbers());

        FBooking fBooking = modelMapper.map(resource, FBooking.class);
        fBooking.setModifiedTimestamp(new Date().getTime() * -1);
        fBooking.setCreatedTimestamp(fBookingOld.getCreatedTimestamp());
        DatabaseReference dbr = dbRef.child(FBooking.key).child(bookingKey);
        return DBHandle.insertDataToDB(fBooking, dbr);
    }

    public ResponseEntity deleteBookingByRegNumber(String seasonCode, String tripCode, int regNumber) {
        String bookingKey = seasonCode + "_" + tripCode + "_" + regNumber;
        ValidateResource.validateDataAvaiAndReturn(FBooking.class, true, dbRef.child(FBooking.key).child(bookingKey), String.format(bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));
        DatabaseReference dbr = dbRef.child(FBooking.key).child(bookingKey);
        return DBHandle.deleteDataFromDB(dbr);
    }

    public ResponseEntity<InputStreamResource> getMdyApplicationPdf(String seasonCode, String tripCode, int regNumber) {
        ResponseEntity<BookingDTO> res = getBookingByRegNumber(seasonCode, tripCode, regNumber);
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            throw new ResourceAccessException(String.format(bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));
        }

        MdyApplication pdfObj = modelMapper.map(res.getBody(), MdyApplication.class);
        String fileName = res.getBody().getSeasonCode() + "_" + res.getBody().getTripCode() + "_" + res.getBody().getRegistrationNumber();

        try (ByteArrayInputStream byteArrayInputStream = MdyApplicationHandler.generatePdfAsByteArray(pdfObj)) {
            InputStreamResource in = new InputStreamResource(byteArrayInputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "MahameghaApplication" + fileName + ".pdf")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(in);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<InputStreamResource> getPassportFormPdf(String seasonCode, String tripCode, int regNumber) {
        ResponseEntity<BookingDTO> res = getBookingByRegNumber(seasonCode, tripCode, regNumber);
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            throw new ResourceAccessException(String.format(bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));
        }

        PassportForm ppObj = modelMapper.map(res.getBody(), PassportForm.class);
        String fileName = res.getBody().getSeasonCode() + "_" + res.getBody().getTripCode() + "_" + res.getBody().getRegistrationNumber();

        try (ByteArrayInputStream byteArrayInputStream = PasspostFormHandler.generatePdfAsByteArray(ppObj)) {
            InputStreamResource in = new InputStreamResource(byteArrayInputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "PassportApplication" + fileName + ".pdf")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(in);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Boolean> isBookingByRegNumberExist(String seasonCode, String tripCode, int regNumber) {
        ResponseEntity<BookingDTO> bkByRegNum = getBookingByRegNumber(seasonCode, tripCode, regNumber);
        if (bkByRegNum.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(bkByRegNum.getStatusCode());
        }
        if (bkByRegNum.getBody() != null) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
