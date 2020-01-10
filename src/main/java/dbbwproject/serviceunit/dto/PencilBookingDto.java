package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PencilBookingDto {
    @NotBlank
    @Size(max = 10, message = "season code can not exceed 10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "season code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the season.", example = "s1", required = true)
    private String seasonCode;

    @NotBlank
    @Size(max = 5, message = "trip code can not exceed 5 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "trip code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the trip.", example = "t12", required = true)
    private String tripCode;

    @NotBlank
    @Size(max = 100, message = "person name can not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "person name can only contain A-Z,a-z letters")
    @ApiModelProperty(notes = "Name of the customer of pencil booking. Note that (seasonCode,tripCode,Name) is unique", example = "Gunasekara", required = true)
    private String personName;

    @NotBlank(message = "telephone number can not be empty")
    @ApiModelProperty(notes = "Telephone Number of the customer of pencil booking.", example = "771650589", required = true)
    private String tpNo;

    @Positive
    @ApiModelProperty(notes = "Count of the people who supposed to attend to trip with the customer of pencil booking.", example = "5", required = true)
    private int seatCount;

    @NotNull(message = "regNumbers cannot be null.")
    @NotEmpty(message = "regNumbers cannot be empty.")
    @ApiModelProperty(notes = "Registration numbers of people who supposed to attend to trip with the customer of pencil booking." +
            "suppose 5 people will be attend with the pencil booking customer \'Gunasekara\'. Then 5 registration numbers will be issued to the pencil booking." +
            "(FYI: Registration number range is 1 to trip's seat count)", example = "[1,10,102,105,109]", required = true)
    private List<Integer> regNumbers;

    @NotBlank
    @Pattern(regexp = "^\\d{4}[/\\-](0?[1-9]|1[012])[/\\-](0?[1-9]|[12][0-9]|3[01])$"
            , message = "Meeting Date should be  in yyyy-MM-dd format")
    @ApiModelProperty(notes = "Date which the customer of pencil booking should come to Mahamega office with documents for first meet up in yyyy-MM-dd format.", example = "2019-12-21", required = true)
    private String meetUpDate;

    @ApiModelProperty(notes = "Status whether the customer of pencil booking arrived for first meet up (on or before meetup date).", example = "CUSTOMER_NOT_ARRIVED")
    private PencilBookingStatus pencilBookingStatus;

}
