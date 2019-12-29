package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.dto.DropDownDTO;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.service.SeasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@Api(value = "Season Management")
@RequestMapping("/resource-management/")
public class SeasonController {
    private SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @ApiOperation(value = "Retrieve a list of all season status for drop down", response = ResponseEntity.class)
    @GetMapping("seasons/season-status")
    public ResponseEntity<List<DropDownDTO>> getAllSeasonStatus() {
        List<DropDownDTO> collect = Arrays.stream(SeasonStatus.values()).map(s -> new DropDownDTO(s, s.toString())).collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve a list of all seasons", response = ResponseEntity.class)
    @GetMapping("seasons")
    public ResponseEntity<List<SeasonDTO>> getAllSeasons(@RequestParam(name = "lastSeasonCode", required = false, defaultValue = "") String lastSeasonCode, @RequestParam("size") int size) {
        return seasonService.getAllSeasonsUponLimit(lastSeasonCode, size);
    }

    @ApiOperation(value = "Retrieve a list of all season codes for drop down", response = ResponseEntity.class)
    @GetMapping("season-codes")
    public ResponseEntity<List<DropDownDTO>> getAllSeasonCodes(@RequestParam(name = "lastSeasonCode", required = false, defaultValue = "") String lastSeasonCode, @RequestParam("size") int size) {
        ResponseEntity<List<SeasonDTO>> res = seasonService.getAllSeasonsUponLimit(lastSeasonCode, size);
        if (res.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(res.getStatusCode());
        }
        if (CollectionUtils.isEmpty(res.getBody())) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<DropDownDTO> collect = res.getBody().stream().map(s -> new DropDownDTO(s.getCode(), s.getCode())).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    @ApiOperation(value = "Retrieve season by code", response = ResponseEntity.class)
    @GetMapping("seasons/{code}")
    public ResponseEntity<SeasonDTO> getSeasonByCode(@PathVariable String code) {
        return seasonService.getSeasonByCode(code);
    }

    @ApiOperation(value = "Modify existing season by code", response = ResponseEntity.class)
    @PutMapping("seasons/{code}")
    public ResponseEntity modifySeasonByCode(@PathVariable String code, @Valid @RequestBody SeasonDTO resource) {
        return seasonService.modifySeasonByCode(code, resource);
    }

    @ApiOperation(value = "Create a season ", response = ResponseEntity.class)
    @PostMapping("seasons")
    public ResponseEntity createNewSeason(@Valid @RequestBody SeasonDTO resource) {
        return seasonService.createNewSeason(resource);
    }

    @ApiOperation(value = "Delete a season ", response = ResponseEntity.class)
    @DeleteMapping("/seasons/{code}")
    public ResponseEntity deleteSeasonByCode(@PathVariable String code) {
        return seasonService.deleteSeasonByCode(code);
    }
}
