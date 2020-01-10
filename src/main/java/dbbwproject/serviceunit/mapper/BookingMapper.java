package dbbwproject.serviceunit.mapper;

import dbbwproject.serviceunit.dao.Booking;
import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dao.RegNumber;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.BookingDto;
import dbbwproject.serviceunit.pdfhandler.application.MdyApplication;
import dbbwproject.serviceunit.pdfhandler.passport.PassportForm;
import dbbwproject.serviceunit.repository.RegNumberRepository;
import dbbwproject.serviceunit.service.MCons;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static dbbwproject.serviceunit.service.ValidateResource.throwEx;
import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public abstract class BookingMapper {
    @Autowired
    RegNumberRepository rr;

    @Autowired
    DBUtil dbUtil;

    public abstract List<Booking> mapBkDtoToBkList(List<BookingDto> b);

    @Mapping(target = "registrationNumber", ignore = true)
    public abstract Booking mapBkDtoToBk(BookingDto b);

    public abstract List<BookingDto> mapBkToBkDtoList(List<Booking> booking);

    @Mapping(target = "seasonCode", source = "registrationNumber.trip.season.code")
    @Mapping(target = "tripCode", source = "registrationNumber.trip.code")
    @Mapping(target = "pbPersonName", source = "registrationNumber.pencilBooking.personName")
    @Mapping(target = "tripStartDate", source = "registrationNumber.trip.startDate")
    @Mapping(target = "registrationNumber", source = "registrationNumber.regNumber")
    public abstract BookingDto mapBkToBkDto(Booking booking);

    @Mapping(target = "registrationNumber", ignore = true)
    public abstract Booking modBkDtoToBk(BookingDto b, @MappingTarget Booking bk);

    @AfterMapping
    Booking fillRegNum(BookingDto b, @MappingTarget Booking bk) {
        Trip trip = dbUtil.getTrip(b.getSeasonCode(), b.getTripCode());
        valArg(trip == null, String.format(MCons.tripNotExist, b.getTripCode(), b.getSeasonCode()));
        PencilBooking pb = dbUtil.getPencilBooking(b.getSeasonCode(), b.getTripCode(), b.getPbPersonName());
        valArg(pb == null, String.format(MCons.penBkNotExist, b.getSeasonCode(), b.getTripCode(), b.getPbPersonName()));

        RegNumber rg = rr.findRegNumberByPencilBookingAndRegNumber(pb, b.getRegistrationNumber())
                .orElseThrow(throwEx(String.format(MCons.invalidRegNum, b.getRegistrationNumber(), pb.getPersonName())));

        bk.setRegistrationNumber(rg);
        return bk;
    }

    @Mapping(target = "registrationNumber", ignore = true)
    public abstract MdyApplication mapApp(Booking b);

    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "dualCitizen", ignore = true)
    public abstract PassportForm mapPassportForm(Booking b);
}
