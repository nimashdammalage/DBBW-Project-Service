package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeasonService extends AbstractService {
    private final java.lang.reflect.Type seasonDTOListType;

    @Autowired
    public SeasonService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
        seasonDTOListType = new TypeToken<List<SeasonDTO>>() {
        }.getType();
    }

    public ResponseEntity<List<SeasonDTO>> getAllSeasonsUponLimit(String lastSeasonCode, int size) {
        Query query;
        if (lastSeasonCode == null || lastSeasonCode.isEmpty()) {
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
        fseason.setModifiedTimestamp(new Date().getTime());
        fseason.setCreatedTimestamp(fSeasonOld.getCreatedTimestamp());
        DatabaseReference dbr = dbRef.child(FSeason.key).child(key);
        return DBHandle.insertDataToDB(fseason, dbr);
    }

    public ResponseEntity createNewSeason(SeasonDTO resource) {
        String key = resource.getCode();
        ValidateResource.validateDataAvaiAndReturn(FSeason.class, false, dbRef.child(FSeason.key).child(key), String.format(seasonAlreadyExist, key));
        ValidateResource.validateDataAvaiAndReturn(resource.getStatus() == SeasonStatus.CURRENT, FSeason.class, false, dbRef.child(FSeason.key).orderByChild(FSeason.STATUS).equalTo(SeasonStatus.CURRENT.toString()), duplicateCurrentSeason);

        FSeason fseason = modelMapper.map(resource, FSeason.class);
        fseason.setCreatedTimestamp(new Date().getTime());
        DatabaseReference dbr = dbRef.child(FSeason.key).child(key);
        return DBHandle.insertDataToDB(fseason, dbr);
    }

    public ResponseEntity deleteSeasonByCode(String code) {
        ValidateResource.validateDataAvaiAndReturn(FSeason.class, true, dbRef.child(FSeason.key).child(code), String.format(seasonNotExist, code));
        ValidateResource.validateDataAvaiAndReturn(FTrip.class, false, dbRef.child(FTrip.key).orderByChild(FTrip.SEASON_CODE).equalTo(code), String.format(linkedTripExists, code));

        DatabaseReference dbr = dbRef.child(FSeason.key).child(code);
        return DBHandle.deleteDataFromDB(dbr);
    }
}
