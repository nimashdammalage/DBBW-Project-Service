package dbbwproject.serviceunit.mapper;

import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dao.RegNumber;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.PencilBookingDto;
import dbbwproject.serviceunit.service.MCons;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public abstract class PencilBookingMapper {
    @Autowired
    DBUtil dbUtil;

    public abstract List<PencilBooking> mapPbDtoToPbList(List<PencilBookingDto> pencilBookingDTO);

    @Mapping(target = "regNumbers", ignore = true)
    public abstract PencilBooking mapPbDtoToPb(PencilBookingDto pencilBookingDTO);

    public abstract List<PencilBookingDto> mapPbToPbDtoList(List<PencilBooking> pencilBooking);

    @Mapping(target = "seasonCode", source = "trip.season.code")
    @Mapping(target = "tripCode", source = "trip.code")
    public abstract PencilBookingDto mapPbToPbDto(PencilBooking pencilBooking);

    @Mapping(target = "regNumbers", ignore = true)
    public abstract PencilBooking modPbDtoToPb(PencilBookingDto p, @MappingTarget PencilBooking pb);

    Integer toRegInt(RegNumber r) {
        return r.getRegNumber();
    }

    @AfterMapping
    PencilBooking fillTripAndRegNums(PencilBookingDto p, @MappingTarget PencilBooking t) {
        //setting trip
        Trip trip = dbUtil.getTrip(p.getSeasonCode(), p.getTripCode());
        valArg(trip == null, String.format(MCons.tripNotExist, p.getTripCode(), p.getSeasonCode()));
        t.setTrip(trip);

        //setting reg numbers
        List<RegNumber> regs = new ArrayList<>();
        for (Integer regNum : p.getRegNumbers()) {
            regs.add(new RegNumber(t, trip, regNum));
        }
        t.setRegNumbers(regs);
        return t;
    }
}
