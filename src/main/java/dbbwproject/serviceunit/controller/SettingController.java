package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.SettingsDTO;
import dbbwproject.serviceunit.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Settings Management")
@RequestMapping("/resource-management/")
public class SettingController {
    private SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @ApiOperation(value = "Retrieve all settings", response = ResponseEntity.class)
    @GetMapping("settings")
    public ResponseEntity<SettingsDTO> getSettings() {
        return settingService.getSettings();
    }

    @ApiOperation(value = "Modify settings", response = ResponseEntity.class)
    @PostMapping("settings")
    public ResponseEntity modifySettings(@Valid @RequestBody SettingsDTO resource) {
        return settingService.modifySettings(resource);
    }
}
