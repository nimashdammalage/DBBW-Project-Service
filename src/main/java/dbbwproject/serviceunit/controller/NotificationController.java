package dbbwproject.serviceunit.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import dbbwproject.serviceunit.dao.FNotification;
import dbbwproject.serviceunit.dto.NotificationDTO;
import dbbwproject.serviceunit.dto.TripDTO;
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
import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Notification Management", description = "handling user notification operations")
@RequestMapping("/resource-management/")
public class NotificationController extends ResourseController {

    @Autowired
    public NotificationController(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
    }

    @ApiOperation(value = "Retrieve a list of all notifications", response = ResponseEntity.class)
    @GetMapping("notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@RequestParam("unreadOnly") boolean unreadOnly) {
        ResponseEntity<List<FNotification>> fNotifications;
        if (unreadOnly) {
            fNotifications = DBHandle.retrieveDataList(FNotification.class, dbRef.child(FNotification.key).orderByChild("read").equalTo(false));
        } else {
            fNotifications = DBHandle.retrieveDataList(FNotification.class, dbRef.child(FNotification.key));
        }
        if (fNotifications.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fNotifications.getStatusCode());
        }
        java.lang.reflect.Type notificationDTOListType = new TypeToken<List<NotificationDTO>>() {
        }.getType();
        if (fNotifications.getBody() == null || fNotifications.getBody().isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<NotificationDTO> map = modelMapper.map(fNotifications.getBody(), notificationDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("notifications")
    @ApiOperation(value = "Create a notification", response = ResponseEntity.class)
    public ResponseEntity<TripDTO> createNewNotification(@Valid @RequestBody NotificationDTO resource) {
        String key = resource.getNotificationId();
        FNotification fNotification = modelMapper.map(resource, FNotification.class);

        DatabaseReference dbr = dbRef.child(FNotification.key).child(key);
        return DBHandle.insertDataToDB(fNotification, dbr);
    }
}
