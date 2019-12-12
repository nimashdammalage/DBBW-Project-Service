package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.response.ErrStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import dbbwproject.serviceunit.dao.FSeason;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Season Management", description = "handling season resource operations")
@RequestMapping("/resource-management/")
public class SeasonController extends ResourseController {
    private static final String localResourcePath = "seasons";

    @Autowired
    public SeasonController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp, localResourcePath);
    }

    @ApiOperation(value = "Retrieve a list of all season status", response = ResponseWrapperList.class)
    @GetMapping("season-status")
    public ResponseWrapperList<SeasonStatus> getAllSeasonStatus() {
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, Arrays.asList(SeasonStatus.values()));
    }

    @ApiOperation(value = "Retrieve a list of all seasons", response = ResponseWrapperList.class)
    @GetMapping("seasons")
    public ResponseWrapperList<SeasonDTO> getAllSeasons() {
        ResponseWrapperList<FSeason> result = retrieveDataList(FSeason.class, dbRef);
        if (result.getStatus() == ErrStatus.ERROR) {
            return new ResponseWrapperList<>(ErrStatus.ERROR, null, result.getErrorMsg());
        }
        java.lang.reflect.Type seasonDTOListType = new TypeToken<List<SeasonDTO>>() {
        }.getType();
        return new ResponseWrapperList<>(ErrStatus.SUCCESS, modelMapper.map(result.getResponseObjectList(), seasonDTOListType));
    }

    @ApiOperation(value = "Retrieve season by code", response = ResponseWrapper.class)
    @GetMapping("seasons/{code}")
    public ResponseWrapper<SeasonDTO> getSeasonByCode(@PathVariable String code) {
        ResponseWrapper<FSeason> res = retrieveData(FSeason.class, dbRef.child(code));
        if (res.getResponseObject() == null) {
            return new ResponseWrapper<>(res.getStatus(), null, res.getErrorMsg());
        }
        return new ResponseWrapper<>(res.getStatus(), modelMapper.map(res.getResponseObject(), SeasonDTO.class));
    }

    @ApiOperation(value = "Modify existing season by code", response = ResponseWrapper.class)
    @PutMapping("seasons/{code}")
    public ResponseWrapper<SeasonDTO> modifySeasonByCode(@PathVariable String code, @RequestBody SeasonDTO resource) {
        if (!code.equals(resource.getCode())) {
            return new ResponseWrapper<>(ErrStatus.ERROR, null, "season's code: " + resource.getCode() + " and code in url: " + code + " does not match");
        }

        FSeason fseason = modelMapper.map(resource, FSeason.class);
        String key = resource.getCode();
        String errMsg = "season with code: " + resource.getCode() + " does not exist in database";
        ResponseWrapper<SeasonDTO> res = retrieveDataAvailability(SeasonDTO.class, FSeason.class, dbRef.child(key), errMsg);
        if (res.getStatus() == ErrStatus.DATA_UNAVAILABLE) {
            res.setStatus(ErrStatus.ERROR);
            return res;
        }
        DatabaseReference dbr = dbRef.child(key);
        return insertDataToDB(SeasonDTO.class, fseason, dbr);
    }

    @ApiOperation(value = "Create a season ", response = ResponseWrapper.class)
    @PostMapping("seasons")
    public ResponseWrapper<SeasonDTO> createNewSeason(@RequestBody SeasonDTO resource) {
        FSeason fseason = modelMapper.map(resource, FSeason.class);
        String key = resource.getCode();
        String errMsg = "season with code: " + resource.getCode() + " already exists in database";
        ResponseWrapper<SeasonDTO> res = retrieveDataAvailability(SeasonDTO.class, FSeason.class, dbRef.child(key), errMsg);
        if (res.getStatus() == ErrStatus.DATA_AVAILABLE) {
            res.setStatus(ErrStatus.ERROR);
            return res;
        }
        DatabaseReference dbr = dbRef.child(key);
        return insertDataToDB(SeasonDTO.class, fseason, dbr);
    }

    @ApiOperation(value = "Delete a season ", response = ResponseWrapper.class)
    @DeleteMapping("/seasons/{code}")
    public ResponseWrapper<SeasonDTO> deleteSeasonByCode(@PathVariable String code) {
        String errMsg = "season with code: " + code + " does not exist in database for deletion";
        ResponseWrapper<SeasonDTO> res = retrieveDataAvailability(SeasonDTO.class, FSeason.class, dbRef.child(code), errMsg);
        if (res.getStatus() == ErrStatus.DATA_UNAVAILABLE) {
            res.setStatus(ErrStatus.ERROR);
            return res;
        }
        DatabaseReference dbr = dbRef.child(code);
        return deleteDataFromDB(SeasonDTO.class, dbr);
    }

}
