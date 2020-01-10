package dbbwproject.serviceunit.dto.datatable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Search {
    @ApiModelProperty ("Search value to apply to this specific column.")
    private String value;
    @ApiModelProperty(notes = "true if the global filter should be treated as a regular expression for advanced searching, false otherwise.")
    private boolean regex;
}
