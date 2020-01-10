package dbbwproject.serviceunit.mapper;

import dbbwproject.serviceunit.dao.Season;
import dbbwproject.serviceunit.dao.Trip;
import dbbwproject.serviceunit.dto.TripDto;
import dbbwproject.serviceunit.repository.SeasonRepository;
import dbbwproject.serviceunit.service.MCons;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static dbbwproject.serviceunit.service.ValidateResource.throwEx;

@Component
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public abstract class TripMapper {
    @Autowired
    protected SeasonRepository sr;

    public abstract List<Trip> mapTdtoToTList(List<TripDto> tripDTO);

    @Mapping(target = "season", source = "seasonCode")
    public abstract Trip mapTdtoToT(TripDto tripDTO);

    public abstract List<TripDto> mapTToTdtoList(List<Trip> trip);

    @Mapping(target = "seasonCode", source = "season.code")
    public abstract TripDto mapTToTdto(Trip trip);

    @Mapping(target = "season", source = "seasonCode")
    public abstract Trip modTdtoToT(TripDto tripDTO, @MappingTarget Trip trip);

    Season mapByCode(String seasonCode) {
        return sr.findSeasonByCode(seasonCode)
                .orElseThrow(throwEx(String.format(MCons.seasonNotExist, seasonCode)));
    }


}
