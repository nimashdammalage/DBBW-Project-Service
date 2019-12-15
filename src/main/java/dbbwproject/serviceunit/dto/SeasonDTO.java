package dbbwproject.serviceunit.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeasonDTO {
    @NotBlank(message = "season code cannot be null")
    @Size(max = 50, message = "season code can not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "season code can only contain A-Z,a-z and 0-9 letters")
    @ApiModelProperty(notes = "Code of the season (unique)", required = true, example = "seasonCode1")
    private String code;

    @ApiModelProperty(notes = "Status of the season(Ex:COMPLETED,CURRENT)", allowableValues = "COMPLETED,UP_COMING,CURRENT")
    private SeasonStatus status;
}
