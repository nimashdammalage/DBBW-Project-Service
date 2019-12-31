package dbbwproject.serviceunit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.datatable.Column;
import dbbwproject.serviceunit.dto.datatable.DTReqDTO;
import dbbwproject.serviceunit.dto.datatable.DTResponse;
import dbbwproject.serviceunit.filter.FSeasonFilter;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.*;
import javax.validation.constraints.Positive;
import java.util.*;

public class SeasonService extends AbstractService {
    private final java.lang.reflect.Type seasonDTOListType;
    private final ObjectMapper objectMapper;

    @Autowired
    public SeasonService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
        seasonDTOListType = new TypeToken<List<SeasonDTO>>() {
        }.getType();
        objectMapper = new ObjectMapper();
    }

    public ResponseEntity<List<SeasonDTO>> getAllSeasonsUponLimit(String lastSeasonCode, int size) {
        Query query;
        if (StringUtils.isBlank(lastSeasonCode)) {
            query = dbRef.child(FSeason.key).orderByKey().limitToFirst(size);
        } else {
            query = dbRef.child(FSeason.key).orderByKey().startAt(lastSeasonCode).limitToFirst(size);
        }
        ResponseEntity<List<FSeason>> fSeasons = DBHandle.retrieveDataList(FSeason.class, query);
        if (fSeasons.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fSeasons.getStatusCode());
        }
        if (fSeasons.getBody() == null || fSeasons.getBody().isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<SeasonDTO> map = modelMapper.map(fSeasons.getBody(), seasonDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<SeasonDTO> getSeasonByCode(String seasonCode) {
        ResponseEntity<FSeason> res = DBHandle.retrieveData(FSeason.class, dbRef.child(FSeason.key).child(seasonCode));
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(modelMapper.map(res.getBody(), SeasonDTO.class), HttpStatus.OK);
    }

    public ResponseEntity modifySeasonByCode(String seasonCode, SeasonDTO resource) {
        ValidateResource.validateArgument(!seasonCode.equals(resource.getCode()), "season's code: " + resource.getCode() + " and code in url: " + seasonCode + " does not match");
        String key = resource.getCode();
        FSeason fSeasonOld = ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(key), String.format(seasonNotExist, key));
        ValidateResource.validateDataAvaiAndReturn(resource.getStatus() == SeasonStatus.CURRENT, FSeason.class, false, dbRef.child(FSeason.key).orderByChild(FSeason.STATUS).equalTo(SeasonStatus.CURRENT.toString()), duplicateCurrentSeason);

        FSeason fseason = modelMapper.map(resource, FSeason.class);
        fseason.setModifiedTimestamp(new Date().getTime() * -1);
        fseason.setCreatedTimestamp(fSeasonOld.getCreatedTimestamp());
        DatabaseReference dbr = dbRef.child(FSeason.key).child(key);
        return DBHandle.insertDataToDB(fseason, dbr);
    }

    public ResponseEntity createNewSeason(SeasonDTO resource) {
        String key = resource.getCode();
        ValidateResource.validateDataAvaiAndReturn(FSeason.class, false, dbRef.child(FSeason.key).child(key), String.format(seasonAlreadyExist, key));
        ValidateResource.validateDataAvaiAndReturn(resource.getStatus() == SeasonStatus.CURRENT, FSeason.class, false, dbRef.child(FSeason.key).orderByChild(FSeason.STATUS).equalTo(SeasonStatus.CURRENT.toString()), duplicateCurrentSeason);

        FSeason fseason = modelMapper.map(resource, FSeason.class);
        fseason.setCreatedTimestamp(new Date().getTime() * -1);
        DatabaseReference dbr = dbRef.child(FSeason.key).child(key);
        return DBHandle.insertDataToDB(fseason, dbr);
    }

    public ResponseEntity deleteSeasonByCode(String code) {
        ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(code), String.format(seasonNotExist, code));
        ValidateResource.validateDataAvaiAndReturn(FTrip.class, false, dbRef.child(FTrip.key).orderByChild(FTrip.SEASON_CODE).equalTo(code), String.format(linkedTripExists, code));

        DatabaseReference dbr = dbRef.child(FSeason.key).child(code);
        return DBHandle.deleteDataFromDB(dbr);
    }

    public ResponseEntity<Boolean> isSeasonByCodeExist(String code) {
        ResponseEntity<SeasonDTO> seasonByCode = getSeasonByCode(code);
        if (seasonByCode.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(seasonByCode.getStatusCode());
        }
        if (seasonByCode.getBody() != null) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    public ResponseEntity<DTResponse<SeasonDTO>> getAllSeasonsForDT(String dtReqDTOStr) {
        DTReqDTO dtReqDTO;
        try {
            dtReqDTO = objectMapper.readValue(dtReqDTOStr, DTReqDTO.class);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<DTReqDTO>> violations = validator.validate(dtReqDTO);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            dtReqDTO.validate();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("can not parse dtReqDTO to DTReqDTO object");
        }

        //extract paginationReference value if any:
        Optional<Column> first = dtReqDTO.getColumns().stream().filter(c -> DTReqDTO.lastCreatedTimeStamp.equals(c.getData())).findFirst();
        final int reqLength = dtReqDTO.getLength();
        long lastCreatedTimeStamp = first.isPresent() ? Long.parseLong(first.get().getName()) : 1L;
        List<FSeason> filteredResult = new ArrayList<>();

        while (filteredResult.size() < reqLength) {
            Query query;
            if (lastCreatedTimeStamp == 1L) {
                query = dbRef.child(FSeason.key).orderByChild(FSeason.CREATED_TIME_STAMP).limitToFirst(reqLength);
            } else {
                query = dbRef.child(FSeason.key).orderByChild(FSeason.CREATED_TIME_STAMP).startAt(lastCreatedTimeStamp).limitToLast(reqLength);
            }
            ResponseEntity<List<FSeason>> fSeasons = DBHandle.retrieveDataList(FSeason.class, query);

            //filter result set
            if (fSeasons.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<>(fSeasons.getStatusCode());
            }
            //no results retrieved, get out of while loop
            if (fSeasons.getBody().size() < 2) {
                break;
            }

            lastCreatedTimeStamp = fSeasons.getBody().get(fSeasons.getBody().size() - 1).getCreatedTimestamp();

            List<FSeason> filteresSeasons = FSeasonFilter.of(dtReqDTO.getColumns()).filterList(fSeasons.getBody());
            for (FSeason filteresSeason : filteresSeasons) {
                if (filteredResult.size() < reqLength) {
                    filteredResult.add(filteresSeason);
                } else {
                    break;
                }
            }
        }

        List<SeasonDTO> map = modelMapper.map(filteredResult, seasonDTOListType);

        DTResponse<SeasonDTO> res = new DTResponse<>();
        res.setData(map);
        res.setDraw(dtReqDTO.getDraw());
        res.setRecordsFiltered(map.size());
        res.setRecordsTotal(map.size());
        return ResponseEntity.ok(res);
    }
}
