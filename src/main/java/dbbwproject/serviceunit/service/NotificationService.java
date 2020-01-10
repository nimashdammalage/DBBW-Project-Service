package dbbwproject.serviceunit.service;

import dbbwproject.serviceunit.dao.Notification;
import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.dto.NotificationDto;
import dbbwproject.serviceunit.mapper.NotificationMapperImpl;
import dbbwproject.serviceunit.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService extends AbstractService {
    private final NotificationRepository noRepository;
    private final NotificationMapperImpl nm;
    private final DBUtil dbUtil;

    @Autowired
    public NotificationService(NotificationRepository noRepository, NotificationMapperImpl nm, DBUtil dbUtil) {
        this.noRepository = noRepository;
        this.nm = nm;
        this.dbUtil = dbUtil;
    }

    public ResponseEntity<List<NotificationDto>> getAllNotifications(int fIndex, int size) {
        List<Notification> not = dbUtil.getNotifications(fIndex, size);
        return ResponseEntity.ok(nm.mapNToNDtoList(not));
    }

    public ResponseEntity createNewNotification(NotificationDto resource) {
        noRepository.save(nm.mapNDtoToN(resource));
        return ResponseEntity.ok().build();
    }
}
