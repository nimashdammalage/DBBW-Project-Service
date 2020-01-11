package dbbwproject.serviceunit.service;

import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.DropDownDto;
import dbbwproject.serviceunit.dto.SeasonDto;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.dto.datatable.DtReqDto;
import dbbwproject.serviceunit.dto.datatable.DtResponse;
import dbbwproject.serviceunit.filter.SeasonFilter;
import dbbwproject.serviceunit.mapper.SeasonMapperImpl;
import dbbwproject.serviceunit.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Service
public class SeasonService extends AbstractService {
    private final SeasonRepository seasonRepository;
    private final SeasonMapperImpl sm;
    private final DBUtil dbUtil;
    private final EntityManagerFactory emf;

    @Autowired
    public SeasonService(SeasonRepository seasonRepository, SeasonMapperImpl sm, DBUtil dbUtil, EntityManagerFactory emf) {
        this.seasonRepository = seasonRepository;
        this.sm = sm;
        this.dbUtil = dbUtil;
        this.emf = emf;
    }

    public ResponseEntity<List<SeasonDto>> getAllSeasonsUponLimit(int fIndex, int size) {
        List<Season> seasons = dbUtil.getSeasons(fIndex, size);
        return ResponseEntity.ok(sm.mapSToSdtoList(seasons));
    }

    public ResponseEntity<List<DropDownDto>> getAllSeasonCodesDropDown() {
        List<Season> seasons = dbUtil.getSeasons();
        List<DropDownDto> collect = seasons.stream().map(s -> new DropDownDto(s.getCode(), s.getCode())).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    public ResponseEntity<SeasonDto> getSeasonByCode(String seasonCode) {
        Optional<Season> seasonByCode = seasonRepository.findSeasonByCode(seasonCode);
        if (seasonByCode.isPresent()) {
            return ResponseEntity.ok(sm.mapSToSdto(seasonByCode.get()));
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity modifySeasonByCode(String seasonCode, SeasonDto resource) {
        valArg(!seasonCode.equals(resource.getCode()), String.format(MCons.seasonCodeUrlNotMatch, resource.getCode(), seasonCode));
        Season seasonByCode = seasonRepository.findSeasonByCode(seasonCode).orElseThrow(() -> new ResourceAccessException(String.format(MCons.seasonNotExist, seasonCode)));
        //season with status=CURRENT already exist?
        List<Season> seasonByStatusList = seasonRepository.getSeasonByStatus(SeasonStatus.CURRENT)
                .orElse(new ArrayList<>())
                .stream()
                .filter(s -> !s.getId().equals(seasonByCode.getId()))
                .collect(Collectors.toList());
        valArg(resource.getStatus() == SeasonStatus.CURRENT && !seasonByStatusList.isEmpty(), MCons.duplicateCurrentSeason);
        valForSCompletion(resource.getStatus(), resource.getCode());
        sm.modSdtoToS(resource, seasonByCode);
        seasonRepository.save(seasonByCode);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity createNewSeason(SeasonDto resource) {
        String code = resource.getCode();
        Optional<Season> seasonByCode = seasonRepository.findSeasonByCode(code);
        valArg(seasonByCode.isPresent(), String.format(MCons.seasonAlreadyExist, code));
        List<Season> seasonsByState = seasonRepository.getSeasonByStatus(SeasonStatus.CURRENT).orElse(new ArrayList<>());
        valArg(resource.getStatus() == SeasonStatus.CURRENT && !seasonsByState.isEmpty(), MCons.duplicateCurrentSeason);
        valForSCompletion(resource.getStatus(), resource.getCode());
        seasonRepository.save(sm.mapSdtoToS(resource));
        return ResponseEntity.ok().build();
    }

    private void valForSCompletion(SeasonStatus ss, String code) {
        if (ss != SeasonStatus.COMPLETED) {
            return;
        }
        List<Trip> trips = dbUtil.getTrips(code);
        boolean b = trips.stream().anyMatch(t -> t.getTripStatus() != TripStatus.COMPLETED);
        valArg(b, MCons.incompleteSeason);
    }

    public ResponseEntity deleteSeasonByCode(String code) {
        Season seasonByCode = seasonRepository.findSeasonByCode(code).orElseThrow(() -> new ResourceAccessException(String.format(MCons.seasonNotExist, code)));
        valArg(!seasonByCode.getTrips().isEmpty(), String.format(MCons.linkedTripExists, code));
        seasonRepository.delete(seasonByCode);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Boolean> isSeasonByCodeExist(String code) {
        return ResponseEntity.ok(seasonRepository.findSeasonByCode(code).isPresent());
    }

    public ResponseEntity<DtResponse<SeasonDto>> getAllSeasonsForDT(DtReqDto dtReqDTO) {
        DtResponse<SeasonDto> filteredResult = new SeasonFilter(emf, sm).filter(dtReqDTO);
        return ResponseEntity.ok(filteredResult);
    }
}
