package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import dbbwproject.serviceunit.dao.FSettings;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SettingsDTO;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Settings Management", description = "handling default settings")
@RequestMapping("/resource-management/")
public class SettingController extends ResourseController {

    @Autowired
    public SettingController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
    }

    @ApiOperation(value = "Retrieve all settings", response = ResponseEntity.class)
    @GetMapping("settings")
    public ResponseEntity<SettingsDTO> getSettings() {
        ResponseEntity<FSettings> result = DBHandle.retrieveData(FSettings.class, dbRef.child(FSettings.key));
        if (result.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(result.getStatusCode());
        }
        if (result.getBody() == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(modelMapper.map(result.getBody(), SettingsDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value = "Modify settings", response = ResponseEntity.class)
    @PostMapping("settings")
    public ResponseEntity<SeasonDTO> modifySettings(@Valid @RequestBody SettingsDTO resource) {
        FSettings fsettings = modelMapper.map(resource, FSettings.class);
        DatabaseReference dbr = dbRef.child(FSettings.key);
        return DBHandle.insertDataToDB(fsettings, dbr);
    }
}
