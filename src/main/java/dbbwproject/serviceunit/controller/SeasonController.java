package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dao.FSeason;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Season Management", description = "handling season resource operations")
@RequestMapping("/resource-management/")
public class SeasonController extends ResourseController {

    @Autowired
    public SeasonController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
    }

    @ApiOperation(value = "Retrieve a list of all season status", response = ResponseEntity.class)
    @GetMapping("seasons/season-status")
    public ResponseEntity<List<SeasonStatus>> getAllSeasonStatus() {
        return new ResponseEntity<>(Arrays.asList(SeasonStatus.values()), HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve a list of all seasons", response = ResponseEntity.class)
    @GetMapping("seasons")
    public ResponseEntity<List<SeasonDTO>> getAllSeasons() {
        ResponseEntity<List<FSeason>> fSeasons = DBHandle.retrieveDataList(FSeason.class, dbRef.child(FSeason.key));
        if (fSeasons.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fSeasons.getStatusCode());
        }
        java.lang.reflect.Type seasonDTOListType = new TypeToken<List<SeasonDTO>>() {
        }.getType();
        if (fSeasons.getBody() == null || fSeasons.getBody().isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<SeasonDTO> map = modelMapper.map(fSeasons.getBody(), seasonDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve season by code", response = ResponseEntity.class)
    @GetMapping("seasons/{code}")
    public ResponseEntity<SeasonDTO> getSeasonByCode(@PathVariable String code) {
        ResponseEntity<FSeason> res = DBHandle.retrieveData(FSeason.class, dbRef.child(FSeason.key).child(code));
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (res.getBody() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(modelMapper.map(res.getBody(), SeasonDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value = "Modify existing season by code", response = ResponseEntity.class)
    @PutMapping("seasons/{code}")
    public ResponseEntity<SeasonDTO> modifySeasonByCode(@PathVariable String code, @Valid @RequestBody SeasonDTO resource) {
        FSeason fseason = modelMapper.map(resource, FSeason.class);
        String key = resource.getCode();
        String seasonNotExist = "season with code: " + resource.getCode() + " does not exists in database for modification";
        String duplicateCurrentSeason = "two seasons can not be in current state";
        ValidateResource.validateArgument(!code.equals(resource.getCode()), "season's code: " + resource.getCode() + " and code in url: " + code + " does not match");
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(key), seasonNotExist);
        ValidateResource.validateDataAvailability(resource.getStatus() == SeasonStatus.CURRENT, FSeason.class, false, dbRef.child(FSeason.key).orderByChild(FSeason.STATUS).equalTo(SeasonStatus.CURRENT.toString()), duplicateCurrentSeason);

        DatabaseReference dbr = dbRef.child(FSeason.key).child(key);
        return DBHandle.insertDataToDB(fseason, dbr);
    }

    @ApiOperation(value = "Create a season ", response = ResponseEntity.class)
    @PostMapping("seasons")
    public ResponseEntity<SeasonDTO> createNewSeason(@Valid @RequestBody SeasonDTO resource) {
        FSeason fseason = modelMapper.map(resource, FSeason.class);
        String key = resource.getCode();
        String seasonExist = "season with code: " + resource.getCode() + " already exists in database";
        String duplicateCurrentSeason = "two seasons can not be in current state";
        ValidateResource.validateDataAvailability(FSeason.class, false, dbRef.child(FSeason.key).child(key), seasonExist);
        ValidateResource.validateDataAvailability(resource.getStatus() == SeasonStatus.CURRENT, FSeason.class, false, dbRef.child(FSeason.key).orderByChild(FSeason.STATUS).equalTo(SeasonStatus.CURRENT.toString()), duplicateCurrentSeason);

        DatabaseReference dbr = dbRef.child(FSeason.key).child(key);
        return DBHandle.insertDataToDB(fseason, dbr);
    }

    @ApiOperation(value = "Delete a season ", response = ResponseEntity.class)
    @DeleteMapping("/seasons/{code}")
    public ResponseEntity<SeasonDTO> deleteSeasonByCode(@PathVariable String code) {
        String key = code;
        String seasonNotExistForDelete = "season with code: " + code + " does not exist in database for deletion";
        String linkedTripExists = "trips with season code: " + code + " found. season can not be deleted";
        ValidateResource.validateDataAvailability(FSeason.class, true, dbRef.child(FSeason.key).child(key), seasonNotExistForDelete);
        ValidateResource.validateDataAvailability(FTrip.class, false, dbRef.child(FTrip.key).orderByChild(FTrip.SEASON_CODE).equalTo(code), linkedTripExists);

        DatabaseReference dbr = dbRef.child(FSeason.key).child(code);
        return DBHandle.deleteDataFromDB(dbr);
    }
}
