package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PencilBookingDTO {
    @ApiModelProperty(notes = "Code of the season.", example = "seasonCode1", required = true)
    private String seasonCode;
    @ApiModelProperty(notes = "Code of the trip.", example = "tripCode1", required = true)
    private String tripCode;
    @ApiModelProperty(notes = "Name of the customer of pencil booking. Note that (seasonCode,tripCode,Name) is unique", example = "Gunasekara", required = true)
    private String personName;
    @ApiModelProperty(notes = "Telephone Number of the customer of pencil booking.", example = "Gunasekara", required = true)
    private String tpNo;
    @ApiModelProperty(notes = "Count of the people who supposed to attend to trip with the customer of pencil booking.", example = "3")
    private int seatCount;
    @ApiModelProperty(notes = "Date which the customer of pencil booking should come to Mahamega office with documents for first meet up.", example = "2020-01-18")
    private String meetUpDate;
    @ApiModelProperty(notes = "Status whether the customer of pencil booking arrived for first meet up (on or before meetup date).", example = "2020-01-18")
    private PencilBookingStatus pencilBookingStatus;
}
