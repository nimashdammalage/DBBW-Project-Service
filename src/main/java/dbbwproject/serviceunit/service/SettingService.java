package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import dbbwproject.serviceunit.dao.FSettings;
import dbbwproject.serviceunit.dto.SettingsDTO;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SettingService extends AbstractService {
    @Autowired
    public SettingService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
    }

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

    public ResponseEntity modifySettings(SettingsDTO resource) {
        FSettings fsettings = modelMapper.map(resource, FSettings.class);
        DatabaseReference dbr = dbRef.child(FSettings.key);
        return DBHandle.insertDataToDB(fsettings, dbr);
    }
}
