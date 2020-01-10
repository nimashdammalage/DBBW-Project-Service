package dbbwproject.serviceunit.service;

import dbbwproject.serviceunit.dao.Settings;
import dbbwproject.serviceunit.dto.SettingsDto;
import dbbwproject.serviceunit.mapper.SettingsMapperImpl;
import dbbwproject.serviceunit.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static dbbwproject.serviceunit.service.ValidateResource.valArg;

@Service
public class SettingService extends AbstractService {
    private final SettingsRepository settingsRepository;
    private final SettingsMapperImpl sm;

    private SettingsDto settingsDTO;

    @Autowired
    public SettingService(SettingsRepository settingsRepository, SettingsMapperImpl sm) {
        this.settingsRepository = settingsRepository;
        this.sm = sm;
        settingsDTO = populateSettingDTO();
    }

    public ResponseEntity<SettingsDto> getSettings() {
        settingsDTO = populateSettingDTO();
        return new ResponseEntity<>(settingsDTO, HttpStatus.OK);
    }

    private SettingsDto populateSettingDTO() {
        List<Settings> all = settingsRepository.findAll();
        valArg(all.isEmpty(), MCons.settingNotExist);
        valArg(all.size() > 1, MCons.multipleSettingExist);
        return sm.mapSToSDto(all.get(0));
    }

    public ResponseEntity modifySettings(SettingsDto resource) {
        List<Settings> all = settingsRepository.findAll();
        valArg(all.isEmpty(), MCons.settingNotExist);
        valArg(all.size() > 1, MCons.multipleSettingExist);
        sm.modSDtoToS(resource, all.get(0));
        settingsRepository.save(all.get(0));
        settingsDTO = populateSettingDTO();
        return ResponseEntity.ok().build();
    }

    SettingsDto getSettingsDTO() {
        return settingsDTO;
    }
}
