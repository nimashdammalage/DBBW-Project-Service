package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.DropDownDto;
import dbbwproject.serviceunit.dto.SeasonDto;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.datatable.DtReqDto;
import dbbwproject.serviceunit.dto.datatable.DtResponse;
import dbbwproject.serviceunit.service.SeasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(value = "Season Management")
@RequestMapping("/resource-management/")
public class SeasonController {
    private final SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @ApiOperation(value = "Retrieve a list of all season status for drop down", response = ResponseEntity.class)
    @GetMapping("seasons/season-status")
    public ResponseEntity<List<DropDownDto>> getAllSeasonStatus() {
        List<DropDownDto> collect = Arrays.stream(SeasonStatus.values()).map(s -> new DropDownDto(s, s.toString())).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @ApiOperation(value = "Retrieve a list of all seasons", response = ResponseEntity.class)
    @GetMapping("seasons")
    public ResponseEntity<List<SeasonDto>> getAllSeasons(@RequestParam(name = "fIndex", required = false, defaultValue = "0") int fIndex, @RequestParam("size") int size) {
        return seasonService.getAllSeasonsUponLimit(fIndex, size);
    }

    @ApiOperation(value = "Retrieve a list of seasons for data table", response = ResponseEntity.class)
    @PostMapping("seasons/datatable")
    public ResponseEntity<DtResponse<SeasonDto>> getAllSeasonsForDT(@RequestBody DtReqDto dtReqDTO) {
        return seasonService.getAllSeasonsForDT(dtReqDTO);
    }

    @ApiOperation(value = "Retrieve a list of all season codes for drop down", response = ResponseEntity.class)
    @GetMapping("season-codes")
    public ResponseEntity<List<DropDownDto>> getAllSeasonCodesDropDown() {
        return seasonService.getAllSeasonCodesDropDown();
    }

    @ApiOperation(value = "Retrieve season by code", response = ResponseEntity.class)
    @GetMapping("seasons/{code}")
    public ResponseEntity<SeasonDto> getSeasonByCode(@PathVariable String code) {
        return seasonService.getSeasonByCode(code);
    }

    @ApiOperation(value = "Retrieve season existence by code", response = ResponseEntity.class)
    @GetMapping("seasons/{code}/exist")
    public ResponseEntity<Boolean> isSeasonByCodeExist(@PathVariable String code) {
        return seasonService.isSeasonByCodeExist(code);
    }

    @ApiOperation(value = "Modify existing season by code", response = ResponseEntity.class)
    @PutMapping("seasons/{code}")
    public ResponseEntity modifySeasonByCode(@PathVariable String code, @Valid @RequestBody SeasonDto resource) {
        return seasonService.modifySeasonByCode(code, resource);
    }

    @ApiOperation(value = "Create a season ", response = ResponseEntity.class)
    @PostMapping("seasons")
    public ResponseEntity createNewSeason(@Valid @RequestBody SeasonDto resource) {
        return seasonService.createNewSeason(resource);
    }

    @ApiOperation(value = "Delete a season ", response = ResponseEntity.class)
    @DeleteMapping("/seasons/{code}")
    public ResponseEntity deleteSeasonByCode(@PathVariable String code) {
        return seasonService.deleteSeasonByCode(code);
    }
}
