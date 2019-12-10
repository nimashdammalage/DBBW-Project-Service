package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripDTO {
    @ApiModelProperty(notes = "Code of the trip. Note that combination of (seasonCode,tripCode) has to be unique", example = "tripCode1", required = true)
    private String code;
    @ApiModelProperty(notes = "Code of the season. Note that combination of (seasonCode,tripCode) has to be unique", example = "seasonCode1", required = true)
    private String seasonCode;
    @ApiModelProperty(notes = "Total cost per one person for the trip in Rupees")
    private int perPersonCost;
    @ApiModelProperty(notes = "Trip start date / Flight departure Date in YYYY-MM-DD format", example = "2018-02-26")
    private String startDate;
    @ApiModelProperty(notes = "Trip end date / Flight arrivel Date in YYYY-MM-DD format", example = "2018-02-26")
    private String endDate;
    @ApiModelProperty(notes = "Date which all passports should be handover to Mahamega office on or before, by customers who participated to trip in YYYY-MM-DD format", example = "2018-02-26")
    private String ppColDate;
    @ApiModelProperty(notes = "Status of the trip(Ex:WORKING)", allowableValues = "WORKING,COMPLETED")
    private TripStatus tripStatus;
}
