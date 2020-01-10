package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.SettingsDto;
import dbbwproject.serviceunit.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "Settings Management")
@RequestMapping("/resource-management/")
public class SettingController {
    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @ApiOperation(value = "Retrieve all settings", response = ResponseEntity.class)
    @GetMapping("settings")
    public ResponseEntity<SettingsDto> getSettings() {
        return settingService.getSettings();
    }

    @ApiOperation(value = "Modify settings", response = ResponseEntity.class)
    @PostMapping("settings")
    public ResponseEntity modifySettings(@Valid @RequestBody SettingsDto resource) {
        return settingService.modifySettings(resource);
    }
}
