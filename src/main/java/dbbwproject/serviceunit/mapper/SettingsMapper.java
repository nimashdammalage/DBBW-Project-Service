package dbbwproject.serviceunit.mapper;

import dbbwproject.serviceunit.dao.Settings;
import dbbwproject.serviceunit.dto.SettingsDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class SettingsMapper {

    public abstract Settings mapSDtoToS(SettingsDto settingsDTO);

    public abstract SettingsDto mapSToSDto(Settings settings);

    public abstract Settings modSDtoToS(SettingsDto s, @MappingTarget Settings st);
}
