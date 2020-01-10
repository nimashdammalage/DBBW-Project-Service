package dbbwproject.serviceunit.dto.datatable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @ApiModelProperty(notes = "Column to which ordering should be applied. " +
            "This is an index reference to the columns array of information that is also submitted to the server")
    @PositiveOrZero(message = "column value must be positive or zero")
    private int column;
    @ApiModelProperty(notes = "Ordering direction for this column. " +
            "It will be asc or desc to indicate ascending ordering or descending ordering, respectively")
    private Dir dir;
}
