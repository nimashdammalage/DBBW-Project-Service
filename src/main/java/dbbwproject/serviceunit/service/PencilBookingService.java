package dbbwproject.serviceunit.service;

import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.DropDownDto;
import dbbwproject.serviceunit.dto.PencilBookingDto;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.mapper.DateMapper;
import dbbwproject.serviceunit.mapper.PencilBookingMapperImpl;
import dbbwproject.serviceunit.repository.PencilBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.stream.Collectors;

import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Service
public class PencilBookingService extends AbstractService {
    private final PencilBookingRepository pencilBookingRepository;
    private final PencilBookingMapperImpl pm;
    private final DBUtil dbUtil;
    private final SettingService settingService;

    @Autowired
    public PencilBookingService(PencilBookingRepository pencilBookingRepository, PencilBookingMapperImpl pm, DBUtil dbUtil, SettingService settingService) {
        this.pencilBookingRepository = pencilBookingRepository;
        this.pm = pm;
        this.dbUtil = dbUtil;
        this.settingService = settingService;
    }

    public ResponseEntity createNewPencilBooking(String seasonCode, String tripCode, PencilBookingDto resource) {
        valArg(!seasonCode.equals(resource.getSeasonCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        valArg(!tripCode.equals(resource.getTripCode()), String.format(MCons.tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        PencilBooking pencilBooking = dbUtil.getPencilBooking(seasonCode,tripCode,resource.getPersonName());
        valArg(pencilBooking != null, String.format(MCons.penBkAlreadyExist, seasonCode, tripCode, resource.getPersonName()));
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        valArg(trip == null, String.format(MCons.tripNotExist, tripCode, seasonCode));
        valArg(trip.getTripStatus() == TripStatus.COMPLETED, String.format(MCons.completeTRipFound, tripCode, seasonCode));
        validateRegNumSequence(resource.getRegNumbers(), seasonCode, tripCode, resource.getPersonName(), trip.getPassengerCount());
        validateMeetingDateMaxExceed(seasonCode, tripCode, resource.getMeetUpDate());

        pencilBookingRepository.save(pm.mapPbDtoToPb(resource));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<PencilBookingDto> getPencilBookingByPersonName(String seasonCode, String tripCode, String personName) {
        PencilBooking pb = dbUtil.getPencilBooking(seasonCode, tripCode, personName);
        if (pb == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(pm.mapPbToPbDto(pb));
    }

    public ResponseEntity<List<PencilBookingDto>> getAllPencilBookingsForTrip(String seasonCode, String tripCode, int fIndex, int size) {
        List<PencilBooking> pbs = dbUtil.getPencilBookings(seasonCode, tripCode, fIndex, size);
        return ResponseEntity.ok(pm.mapPbToPbDtoList(pbs));
    }

    public ResponseEntity<List<DropDownDto>> getAllPencilBookingsForTripDropDown(String seasonCode, String tripCode) {
        List<PencilBooking> pbs = dbUtil.getPencilBookings(seasonCode, tripCode);
        List<DropDownDto> collect = pbs.stream().map(p -> new DropDownDto(p.getPersonName(), p.getPersonName())).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    public ResponseEntity modifyPencilBookingByPersonName(String seasonCode, String tripCode, String personName, PencilBookingDto resource) {
        valArg(!seasonCode.equals(resource.getSeasonCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        valArg(!tripCode.equals(resource.getTripCode()), String.format(MCons.tripCodeUrlNotMatch, resource.getTripCode(), tripCode));
        valArg(!personName.equals(resource.getPersonName()), String.format(MCons.personNameUrlNotMatch, resource.getPersonName(), personName));

        PencilBooking pb = dbUtil.getPencilBooking(seasonCode, tripCode, personName);
        valArg(pb == null, String.format(MCons.penBkNotExist, seasonCode, tripCode, personName));
        valArg(pb.getTrip().getTripStatus() == TripStatus.COMPLETED, String.format(MCons.completeTRipFound, tripCode, seasonCode));
        validateRegNumSequence(resource.getRegNumbers(), seasonCode, tripCode, personName, pb.getTrip().getPassengerCount());
        validateMeetingDateMaxExceed(seasonCode, tripCode, resource.getMeetUpDate());

        pencilBookingRepository.save(pm.mapPbDtoToPb(resource));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deletePencilBookingByPersonName(String seasonCode, String tripCode, String personName) {
        PencilBooking pb = dbUtil.getPencilBooking(seasonCode, tripCode, personName);
        valArg(pb == null, String.format(MCons.penBkNotExist, seasonCode, tripCode, personName));
        pencilBookingRepository.delete(pb);
        return ResponseEntity.ok().build();
    }

    private void validateRegNumSequence(List<Integer> dataSequence, String seasonCode, String tripCode, String personName, int passengerCount) {
        //has duplicates?
        Set<Integer> dataSeqSet = new HashSet<>(dataSequence);
        if (dataSeqSet.size() != dataSequence.size()) {
            throw new ResourceAccessException(MCons.duplicateRegNumbers);
        }

        //exceeds passengerCountMax value?
        OptionalInt max = dataSequence.stream().mapToInt(v -> v).max();
        if (max.isPresent()) {
            if (max.getAsInt() > passengerCount)
                throw new ResourceAccessException(String.format(MCons.RegNumberExceedMaxCount, Integer.toString(max.getAsInt()), Integer.toString(passengerCount)));
        }

        //contains zero?
        OptionalInt min = dataSequence.stream().mapToInt(v -> v).min();
        if (min.isPresent() && min.getAsInt() == 0) {
            throw new ResourceAccessException(MCons.RegNumberConZero);
        }

        //retrieve registration numbers of other pencil bookings
        List<PencilBooking> pencilBookings = dbUtil.getPencilBookings(seasonCode, tripCode);

        //if modification flow, remove already saved pencil booking from consideration
        List<PencilBooking> filteredPbs =
                Optional.ofNullable(pencilBookings)
                        .orElseGet(ArrayList::new)
                        .stream()
                        .filter(pb -> !(personName.equals(pb.getPersonName())
                                && tripCode.equals(pb.getTrip().getCode())
                                && seasonCode.equals(pb.getTrip().getSeason().getCode())))
                        .collect(Collectors.toList());

        //collect data and extract existing booked seats
        Set<Integer> regNumSet = new HashSet<>();
        filteredPbs.forEach(p -> p.getRegNumbers().forEach(r -> regNumSet.add(r.getRegNumber())));

        for (Integer regNum : dataSequence) {
            if (regNumSet.contains(regNum))
                throw new ResourceAccessException(String.format(MCons.alreadyAssignedRegNumber, Integer.toString(regNum)));
        }
    }


    private void validateMeetingDateMaxExceed(String seasonCode, String tripCode, String resourceMeetUpdate) {
        if (!settingService.getSettingsDTO().isMaxPbkCustomersPerDayEnable()) return;
        List<PencilBooking> pbs = dbUtil.getPencilBookings(seasonCode, tripCode);

        long sameMeetupCount = pbs.stream().filter(pbk -> DateMapper.toDateStr(pbk.getMeetUpDate()).equals(resourceMeetUpdate)).count();
        if (sameMeetupCount >= settingService.getSettingsDTO().getMaxPbkCustomersPerDay()) {
            throw new ResourceAccessException(String.format(MCons.maxMeetCountReached, Long.toString(sameMeetupCount)));
        }
    }

    public ResponseEntity<Boolean> isPencilBookingByPersonNameExist(String seasonCode, String tripCode, String personName) {
        ResponseEntity<PencilBookingDto> pbByName = getPencilBookingByPersonName(seasonCode, tripCode, personName);
        if (pbByName.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(pbByName.getStatusCode());
        }
        if (pbByName.getBody() != null) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
