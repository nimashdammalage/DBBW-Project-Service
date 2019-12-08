package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeasonDTO {
    @ApiModelProperty(notes = "Code of the season", required = true)
    private String code;
    @ApiModelProperty(notes = "Status of the season(Ex:COMPLETED,CURRENT)", allowableValues = "COMPLETED,UP_COMING,CURRENT")
    private SeasonStatus status;
}
