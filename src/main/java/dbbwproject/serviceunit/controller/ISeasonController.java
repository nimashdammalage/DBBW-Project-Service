package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.response.ResponseWrapper;
import dbbwproject.serviceunit.dto.response.ResponseWrapperList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/season-management")
public interface ISeasonController {

    @GetMapping("/season-status")
    ResponseWrapperList<SeasonStatus> getAllSeasonStatus();

    @GetMapping("/seasons")
    ResponseWrapperList<SeasonDTO> getAllSeasons();

    @GetMapping("/seasons/{code}")
    ResponseWrapper<SeasonDTO> getSeasonByCode(@PathVariable String code);

    @PutMapping("/seasons/{code}")
    ResponseWrapper<SeasonDTO> modifySeasonByCode(@PathVariable String code, @RequestBody SeasonDTO resource);

    @PostMapping("/seasons")
    ResponseWrapper<SeasonDTO> createNewSeason(@RequestBody SeasonDTO resource);

    @DeleteMapping("/seasons/{code}")
    ResponseWrapper<SeasonDTO> deleteSeasonByCode(@PathVariable String code);
}
