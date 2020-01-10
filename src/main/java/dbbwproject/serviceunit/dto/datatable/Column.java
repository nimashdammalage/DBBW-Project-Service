package dbbwproject.serviceunit.dto.datatable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Column {
    @ApiModelProperty("Column's data source")
    @NotBlank(message = "data value must not be blank")
    private String data;
    @ApiModelProperty(notes = "Column's name")
    @NotNull(message = "name value must not be null")
    private String name;
    @ApiModelProperty(notes = "Flag to indicate if this column is searchable (true) or not (false).")
    private boolean searchable;
    @ApiModelProperty(notes = "Flag to indicate if this column is orderable (true) or not (false)")
    private boolean orderable;
    @NotNull(message = "search value must not be null")
    private @Valid Search search = new Search();
}
