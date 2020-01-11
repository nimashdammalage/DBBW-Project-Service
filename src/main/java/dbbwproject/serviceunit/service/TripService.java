package dbbwproject.serviceunit.service;

import dbbwproject.serviceunit.dao.Booking;
import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.*;
import dbbwproject.serviceunit.dto.datatable.DtReqDto;
import dbbwproject.serviceunit.dto.datatable.DtResponse;
import dbbwproject.serviceunit.filter.TripFilter;
import dbbwproject.serviceunit.mapper.TripMapperImpl;
import dbbwproject.serviceunit.repository.TripRepository;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Service
public class TripService extends AbstractService {
    private final TripRepository tripRepository;
    private final TripMapperImpl tm;
    private final DBUtil dbUtil;
    private final EntityManagerFactory emf;

    @Autowired
    public TripService(TripRepository tripRepository, TripMapperImpl tm, DBUtil dbUtil, EntityManagerFactory emf) {
        this.tm = tm;
        this.dbUtil = dbUtil;
        this.tripRepository = tripRepository;
        this.emf = emf;
    }

    public ResponseEntity<List<Integer>> getBookedSeatNumbersForTrip(String seasonCode, String tripCode) {
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        valArg(trip == null, String.format(MCons.tripNotExist, tripCode, seasonCode));

        List<Integer> regNumList = new ArrayList<>();
        trip.getPencilBookings().forEach(pb -> pb.getRegNumbers().forEach(r -> regNumList.add(r.getRegNumber())));
        return ResponseEntity.ok(regNumList);
    }

    public ResponseEntity<List<TripDto>> getAllTripsForSeason(String seasonCode, int fIndex, int size) {
        List<Trip> trips = dbUtil.getTrips(seasonCode, fIndex, size);
        return ResponseEntity.ok(tm.mapTToTdtoList(trips));
    }

    public ResponseEntity<List<DropDownDto>> getAllTripCodesForSeasonDropDown(String seasonCode) {
        List<Trip> trips = dbUtil.getTrips(seasonCode);
        List<DropDownDto> collect = trips.stream().map(t -> new DropDownDto(t.getCode(), t.getCode())).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    public ResponseEntity<TripDto> getTripByCode(String seasonCode, String tripCode) {
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        if (trip != null) {
            return ResponseEntity.ok(tm.mapTToTdto(trip));
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity modifyTripByCode(String seasonCode, String tripCode, TripDto resource) {
        valArg(!tripCode.equals(resource.getCode()), String.format(MCons.tripCodeUrlNotMatch, resource.getCode(), tripCode));
        valArg(!seasonCode.equals(resource.getSeasonCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        valArg(trip == null, String.format(MCons.tripNotExist, tripCode, seasonCode));
        valArg(trip.getSeason().getStatus() == SeasonStatus.COMPLETED, String.format(MCons.completedSeasonFound, seasonCode));
        valForTCompletion(resource.getTripStatus(), resource.getSeasonCode(), resource.getCode());
        tm.modTdtoToT(resource, trip);
        tripRepository.save(trip);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity createNewTrip(String seasonCode, TripDto resource) {
        valArg(StringUtils.isBlank(resource.getCode()), "trip code can not be empty or null");
        valArg(!seasonCode.equals(resource.getSeasonCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getSeasonCode(), seasonCode));
        Trip trip = dbUtil.getTrip(seasonCode, resource.getCode());
        valArg(trip != null, String.format(MCons.tripAlreadyExists, resource.getCode(), seasonCode));
        Season season = dbUtil.getSeason(seasonCode);
        valArg(season == null, String.format(MCons.seasonNotExist, seasonCode));
        valArg(season.getStatus() == SeasonStatus.COMPLETED, String.format(MCons.completedSeasonFound, seasonCode));
        valForTCompletion(resource.getTripStatus(), resource.getSeasonCode(), resource.getCode());
        tripRepository.save(tm.mapTdtoToT(resource));
        return ResponseEntity.ok().build();
    }

    private void valForTCompletion(TripStatus tripStatus, String seasonCode, String tripCode) {
        if (tripStatus != TripStatus.COMPLETED) {
            return;
        }
        List<PencilBooking> pbs = dbUtil.getPencilBookings(seasonCode, tripCode);
        boolean pb = pbs.stream().anyMatch(p -> p.getPencilBookingStatus() != PencilBookingStatus.CUSTOMER_ARRIVED);
        valArg(pb, MCons.incompleteTRipwithPbs);
        List<Booking> bks = dbUtil.getBookingsForTrip(seasonCode, tripCode);
        boolean bk = bks.stream().anyMatch(b -> b.getBookingStatus() != BookingStatus.COMPLETED);
        valArg(bk, MCons.incompleteTRipwithBks);
    }

    public ResponseEntity deleteTripByCode(String seasonCode, String tripCode) {
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        valArg(trip == null, String.format(MCons.tripNotExist, tripCode, seasonCode));
        valArg(!trip.getPencilBookings().isEmpty(), String.format(MCons.linkedPenBookingExists, tripCode, seasonCode));
        List<Booking> bks = dbUtil.getBookingsForTrip(seasonCode, tripCode);
        valArg(bks.isEmpty(), String.format(MCons.linkedBookingExists, tripCode, seasonCode));
        tripRepository.delete(trip);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Boolean> isTripByCodeExist(String seasonCode, String tripCode) {
        Trip trip = dbUtil.getTrip(seasonCode, tripCode);
        return ResponseEntity.ok(trip != null);
    }

    public ResponseEntity<DtResponse<TripDto>> getAllTripsForDT(DtReqDto dtReqDTO) {
        DtResponse<TripDto> filteredResult = new TripFilter(emf, tm).filter(dtReqDTO);
        return ResponseEntity.ok(filteredResult);
    }
}
