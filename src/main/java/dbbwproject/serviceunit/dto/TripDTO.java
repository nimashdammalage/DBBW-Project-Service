package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripDTO {
    @NotBlank
    @Size(max = 5, message = "trip code can not exceed 5 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "trip code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the trip. Note that combination of (seasonCode,tripCode) has to be unique", example = "t12", required = true)
    private String code;

    @NotBlank
    @Size(max = 10, message = "season code can not exceed 10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "season code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the season. Note that combination of (seasonCode,tripCode) has to be unique", example = "s1", required = true)
    private String seasonCode;

    @Positive(message = "Total cost per one person must be greater than zero")
    @ApiModelProperty(notes = "Total cost per one person for the trip in Rupees",example = "200")
    private int perPersonCost;

    @NotBlank
    @Pattern(regexp = "^\\d{4}[/\\-](0?[1-9]|1[012])[/\\-](0?[1-9]|[12][0-9]|3[01])$"
            , message = "Trip start date / flight departure Date should be  in yyyy-MM-dd format")
    @ApiModelProperty(notes = "Trip start date / flight departure Date in yyyy-MM-dd format", example = "2019-12-21")
    private String startDate;

    @NotBlank
    @Pattern(regexp = "^\\d{4}[/\\-](0?[1-9]|1[012])[/\\-](0?[1-9]|[12][0-9]|3[01])$"
            , message = "Trip end date / flight arrival Date should be  in yyyy-MM-dd format")
    @ApiModelProperty(notes = "Trip end date / flight arrival Date in yyyy-MM-dd format", example = "2019-12-21")
    private String endDate;

    @NotBlank
    @Pattern(regexp = "^\\d{4}[/\\-](0?[1-9]|1[012])[/\\-](0?[1-9]|[12][0-9]|3[01])$"
            , message = "Passport Collection Date should be  in yyyy-MM-dd format")
    @ApiModelProperty(notes = "Date which all passports should be handover to Mahamega office on or before, by customers who participated to trip in yyyy-MM-dd format", example = "2019-12-21")
    private String ppColDate;

    @Positive(message = "Total cost per one person must be greater than zero")
    @ApiModelProperty(notes = "Total number of passengers attend to trip", example = "200")
    private int passengerCount;

    @ApiModelProperty(notes = "Status of the trip(Ex:WORKING)", allowableValues = "WORKING,COMPLETED")
    private TripStatus tripStatus;

    @ApiModelProperty(notes = "Created person", example = "createdUser")
    private String createdBy;

    @ApiModelProperty(notes = "Created person", example = "modifiedUser")
    private String modifiedBy;
}
