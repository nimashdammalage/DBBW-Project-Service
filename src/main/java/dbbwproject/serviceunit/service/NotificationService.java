package dbbwproject.serviceunit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import dbbwproject.serviceunit.dao.FNotification;
import dbbwproject.serviceunit.dto.NotificationDTO;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends AbstractService {
    private final java.lang.reflect.Type notificationDTOListType;

    @Autowired
    public NotificationService(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        super(modelMapper, firebaseApp);
        notificationDTOListType = new TypeToken<List<NotificationDTO>>() {
        }.getType();
    }

    public ResponseEntity<List<NotificationDTO>> getAllNotifications(String lastCreatedDate, int size) {
        ResponseEntity<List<FNotification>> fNotifications;
        if (StringUtils.isBlank(lastCreatedDate)) {
            fNotifications = DBHandle.retrieveDataList(FNotification.class, dbRef.child(FNotification.key).orderByChild(FNotification.CREATED_DATE).limitToLast(size));
        } else {
            long lastDateAsLong = modelMapper.map(lastCreatedDate, Long.class);
            fNotifications = DBHandle.retrieveDataList(FNotification.class, dbRef.child(FNotification.key).orderByChild(FNotification.CREATED_DATE).endAt(lastDateAsLong).limitToLast(size));
        }
        if (fNotifications.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(fNotifications.getStatusCode());
        }

        if (CollectionUtils.isEmpty(fNotifications.getBody())) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        List<NotificationDTO> map = modelMapper.map(fNotifications.getBody(), notificationDTOListType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity createNewNotification(NotificationDTO resource) {
        String key = resource.getNotificationId();
        FNotification fNotification = modelMapper.map(resource, FNotification.class);
        DatabaseReference dbr = dbRef.child(FNotification.key).child(key);
        return DBHandle.insertDataToDB(fNotification, dbr);
    }


    }
