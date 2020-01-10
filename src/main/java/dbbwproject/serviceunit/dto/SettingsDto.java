package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettingsDto {
    @Positive
    @ApiModelProperty(notes = "Default value for per person cost, to be used in Trip details filling", example = "108000")
    private int defaultPerPersonCost;

    private boolean defaultPerPersonCostEnable;

    @Positive
    @ApiModelProperty(notes = "Default gap between trip start date and passport collection date" +
            "to be used in Trip detail filling. ex: if trip date is 2018-2-15 and gap is 10 days, passport collection" +
            "date for that trip is 2018-2-25", example = "100")
    private int defaultPpColDateGap;

    private boolean defaultPpColDateGapEnable;

    @Positive
    @ApiModelProperty(notes = "max customer count who can have same date for meetup", example = "25")
    private int maxPbkCustomersPerDay;

    private boolean maxPbkCustomersPerDayEnable;


}
