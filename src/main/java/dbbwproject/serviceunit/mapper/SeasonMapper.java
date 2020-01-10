package dbbwproject.serviceunit.mapper;

import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dto.SeasonDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public abstract class SeasonMapper {

    public abstract List<Season> mapSdtoToSList(List<SeasonDto> s);

    public abstract Season mapSdtoToS(SeasonDto s);

    public abstract Season modSdtoToS(SeasonDto s, @MappingTarget Season season);

    public abstract List<SeasonDto> mapSToSdtoList(List<Season> seasons);

    public abstract SeasonDto mapSToSdto(Season season);
}
