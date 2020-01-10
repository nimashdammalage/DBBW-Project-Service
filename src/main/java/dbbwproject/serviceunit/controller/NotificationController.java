package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.NotificationDto;
import dbbwproject.serviceunit.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Notification Management")
@RequestMapping("/resource-management/")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Retrieve a list of all notifications", response = ResponseEntity.class)
    @GetMapping("notifications")
    public ResponseEntity<List<NotificationDto>> getAllNotifications(@RequestParam(name = "fIndex", required = false, defaultValue = "0") int fIndex, @RequestParam("size") int size) {
        return notificationService.getAllNotifications(fIndex, size);
    }

    @PostMapping("notifications")
    @ApiOperation(value = "Create a notification", response = ResponseEntity.class)
    public ResponseEntity createNewNotification(@Valid @RequestBody NotificationDto resource) {
        return notificationService.createNewNotification(resource);
    }
}
