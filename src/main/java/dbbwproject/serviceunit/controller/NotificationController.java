package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.NotificationDTO;
import dbbwproject.serviceunit.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@Api(value = "Notification Management")
@RequestMapping("/resource-management/")
public class NotificationController {
    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Retrieve a list of all notifications", response = ResponseEntity.class)
    @GetMapping("notifications")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@RequestParam(name = "lastCreatedDate", required = false) String lastCreatedDate, @RequestParam("size") int size) {
        return notificationService.getAllNotifications(lastCreatedDate, size);
    }

//    @PostMapping("notifications")
//    @ApiOperation(value = "Create a notification", response = ResponseEntity.class)
//    public ResponseEntity createNewNotification(@Valid @RequestBody NotificationDTO resource) {
//        return notificationService.createNewNotification(resource);
//    }
}
