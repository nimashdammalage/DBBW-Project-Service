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
    @ApiModelProperty(notes = "Total cost per one person for the trip in Rupees",example = "200")
    private int perPersonCost;

    @NotBlank
    @Pattern(regexp = "^(\\d{4})\\D?(0[1-9]|1[0-2])\\D?([12]\\d|0[1-9]|3[01])(\\D?([01]\\d|2[0-3])\\D?([0-5]\\d)\\D?([0-5]\\d)?\\D?(\\d{3})?)?$"
            , message = "Trip start date / flight departure Date should be  in yyyy-MM-dd'T'HH:mm:ss.SSS format")
    @ApiModelProperty(notes = "Trip start date / flight departure Date in yyyy-MM-dd'T'HH:mm:ss.SSS format", example = "2019-12-21T13:10:26.641")
    private String startDate;

    @NotBlank
    @Pattern(regexp = "^(\\d{4})\\D?(0[1-9]|1[0-2])\\D?([12]\\d|0[1-9]|3[01])(\\D?([01]\\d|2[0-3])\\D?([0-5]\\d)\\D?([0-5]\\d)?\\D?(\\d{3})?)?$"
            , message = "Trip end date / flight arrival Date should be  in yyyy-MM-dd'T'HH:mm:ss.SSS format")
    @ApiModelProperty(notes = "Trip end date / flight arrival Date in yyyy-MM-dd'T'HH:mm:ss.SSS format", example = "2019-12-21T13:10:26.641")
    private String endDate;

    @NotBlank
    @Pattern(regexp = "^(\\d{4})\\D?(0[1-9]|1[0-2])\\D?([12]\\d|0[1-9]|3[01])(\\D?([01]\\d|2[0-3])\\D?([0-5]\\d)\\D?([0-5]\\d)?\\D?(\\d{3})?)?$"
            , message = "Passport Collection Date should be  in yyyy-MM-dd'T'HH:mm:ss.SSS format")
    @ApiModelProperty(notes = "Date which all passports should be handover to Mahamega office on or before, by customers who participated to trip in yyyy-MM-dd'T'HH:mm:ss.SSS format", example = "2019-12-21T13:10:26.641")
    private String ppColDate;

    @Positive(message = "Total cost per one person must be greater than zero")
    @ApiModelProperty(notes = "Total number of passengers attend to trip", example = "200")
    private int passengerCount;

    @ApiModelProperty(notes = "Status of the trip(Ex:WORKING)", allowableValues = "WORKING,COMPLETED")
    private TripStatus tripStatus;
}
