package dbbwproject.serviceunit.service;

import dbbwproject.serviceunit.dao.Booking;
import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dao.RegNumber;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.BookingDto;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.mapper.BookingMapperImpl;
import dbbwproject.serviceunit.pdfhandler.application.MdyApplication;
import dbbwproject.serviceunit.pdfhandler.application.MdyApplicationHandler;
import dbbwproject.serviceunit.pdfhandler.passport.PassportForm;
import dbbwproject.serviceunit.pdfhandler.passport.PasspostFormHandler;
import dbbwproject.serviceunit.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Service
public class BookingService extends AbstractService {
    private final BookingRepository bookingRepository;
    private final BookingMapperImpl bm;
    private final DBUtil dbUtil;

    @Autowired
    public BookingService(BookingRepository bookingRepository, BookingMapperImpl bm, DBUtil dbUtil) {
        this.bookingRepository = bookingRepository;
        this.bm = bm;
        this.dbUtil = dbUtil;
    }

    public ResponseEntity createNewBooking(String seasonCode, String tripCode, BookingDto resource) {
        valArg(!seasonCode.equals(resource.getSeasonCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        valArg(!tripCode.equals(resource.getTripCode()), String.format(MCons.tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        Booking bk = dbUtil.getBooking(seasonCode, tripCode, resource.getRegistrationNumber());
        valArg(bk != null, String.format(MCons.bookingAlreadyExist, seasonCode, tripCode, resource.getRegistrationNumber()));
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        valArg(trip.getTripStatus() == TripStatus.COMPLETED, String.format(MCons.completeTRipFound, tripCode, seasonCode));
        PencilBooking pb = dbUtil.getPencilBooking(seasonCode, tripCode, resource.getPbPersonName());
        valArg(pb == null, String.format(MCons.penBkNotExist, seasonCode, tripCode, resource.getPbPersonName()));
        validateRegNumber(resource.getRegistrationNumber(), resource.getPbPersonName(), pb.getRegNumbers());

        bookingRepository.save(bm.mapBkDtoToBk(resource));
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<BookingDto> getBookingByRegNumber(String seasonCode, String tripCode, int regNumber) {
        Booking bk = dbUtil.getBooking(seasonCode, tripCode, regNumber);
        if (bk == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(bm.mapBkToBkDto(bk));
    }

    public ResponseEntity<List<BookingDto>> getAllBookingsForTrip(String seasonCode, String tripCode, int fIndex, int size) {
        List<Booking> bks = dbUtil.getBookingsForTrip(seasonCode, tripCode, fIndex, size);
        return new ResponseEntity<>(bm.mapBkToBkDtoList(bks), HttpStatus.OK);
    }

    public ResponseEntity modifyBookingByRegNumber(String seasonCode, String tripCode, int regNumber, BookingDto resource) {
        valArg(!seasonCode.equals(resource.getSeasonCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        valArg(!tripCode.equals(resource.getTripCode()), String.format(MCons.tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        valArg(regNumber != resource.getRegistrationNumber(), String.format(MCons.regNumberUrlNotMatch, Integer.toString(resource.getRegistrationNumber()), Integer.toString(regNumber)));

        Booking bk = dbUtil.getBooking(seasonCode, tripCode, regNumber);
        valArg(bk == null, String.format(MCons.bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));
        valArg(bk.getRegistrationNumber().getTrip().getTripStatus() == TripStatus.COMPLETED, String.format(MCons.completeTRipFound, tripCode, seasonCode));
        PencilBooking pb = dbUtil.getPencilBooking(seasonCode, tripCode, resource.getPbPersonName());
        valArg(pb == null, String.format(MCons.penBkNotExist, seasonCode, tripCode, resource.getPbPersonName()));
        validateRegNumber(resource.getRegistrationNumber(), resource.getPbPersonName(), pb.getRegNumbers());

        bm.modBkDtoToBk(resource, bk);
        bookingRepository.save(bk);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteBookingByRegNumber(String seasonCode, String tripCode, int regNumber) {
        Booking bk = dbUtil.getBooking(seasonCode, tripCode, regNumber);
        valArg(bk == null, String.format(MCons.bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<InputStreamResource> getMdyApplicationPdf(String seasonCode, String tripCode, int regNumber) {
        Booking bk = dbUtil.getBooking(seasonCode, tripCode, regNumber);
        valArg(bk == null, String.format(MCons.bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));

        MdyApplication pdfObj = bm.mapApp(bk);
        String fileName = seasonCode + "_" + tripCode + "_" + regNumber;

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
        Booking bk = dbUtil.getBooking(seasonCode, tripCode, regNumber);
        valArg(bk == null, String.format(MCons.bookingNotExist, seasonCode, tripCode, Integer.toString(regNumber)));

        PassportForm pdfObj = bm.mapPassportForm(bk);
        String fileName = seasonCode + "_" + tripCode + "_" + regNumber;

        try (ByteArrayInputStream byteArrayInputStream = PasspostFormHandler.generatePdfAsByteArray(pdfObj)) {
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
        ResponseEntity<BookingDto> bkByRegNum = getBookingByRegNumber(seasonCode, tripCode, regNumber);
        if (bkByRegNum.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(bkByRegNum.getStatusCode());
        }
        if (bkByRegNum.getBody() != null) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    private void validateRegNumber(int registrationNumber, String pbPersonName, List<RegNumber> regNumList) {
        List<Integer> regNums = regNumList.stream().map(RegNumber::getRegNumber).collect(Collectors.toList());
        valArg(!regNums.contains(registrationNumber), String.format(MCons.invalidRegNum, Integer.toString(registrationNumber), pbPersonName));
    }
}
