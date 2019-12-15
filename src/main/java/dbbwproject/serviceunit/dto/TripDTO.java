package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripDTO {
    @NotBlank
    @Size(max = 50, message = "trip code can not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "trip code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the trip. Note that combination of (seasonCode,tripCode) has to be unique", example = "tripCode1", required = true)
    private String code;

    @NotBlank
    @Size(max = 50, message = "season code can not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "season code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the season. Note that combination of (seasonCode,tripCode) has to be unique", example = "seasonCode1", required = true)
    private String seasonCode;

    @Positive(message = "Total cost per one person must be greater than zero")
    @ApiModelProperty(notes = "Total cost per one person for the trip in Rupees")
    private int perPersonCost;

    @ApiModelProperty(notes = "Trip start date / flight departure Date in YYYY-MM-DD format", example = "2018-02-26")
    private String startDate;

    @ApiModelProperty(notes = "Trip end date / flight arrivel Date in YYYY-MM-DD format", example = "2018-02-26")
    private String endDate;

    @ApiModelProperty(notes = "Date which all passports should be handover to Mahamega office on or before, by customers who participated to trip in YYYY-MM-DD format", example = "2018-02-26")
    private String ppColDate;

    @Positive(message = "Total cost per one person must be greater than zero")
    @ApiModelProperty(notes = "Total number of passengers attend to trip", example = "200")
    private int passengerCount;

    @ApiModelProperty(notes = "Status of the trip(Ex:WORKING)", allowableValues = "WORKING,COMPLETED")
    private TripStatus tripStatus;
}
