package dbbwproject.serviceunit.dto.datatable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DtReqDto {
    @ApiModelProperty(notes = "Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side " +
            "processing requests are drawn in sequence by DataTables (Ajax requests are asynchronous and thus can return " +
            "out of sequence).This is used as part of the draw return parameter")
    @PositiveOrZero(message = "draw value must be positive or zero")
    private int draw;

    @ApiModelProperty(notes = "Paging first record indicator. This is the start point in the current data set " +
            "(0 index based - i.e. 0 is the first record).")
    @PositiveOrZero(message = "start value must be positive or zero")
    private int start;

    @ApiModelProperty(notes = "Number of records that the table can display in the current draw. " +
            "It is expected that the number of records returned will be equal to this number, " +
            "unless the server has fewer records to return. " +
            "Note that this can be -1 to indicate that all records should be returned " +
            "(although that negates any benefits of server-side processing!)")
    private int length;

    @ApiModelProperty(notes = " an array defining all columns in the table.")
    @NotEmpty(message = "columns list cannot be empty.")
    private List<@Valid Column> columns = new ArrayList<>();

    @ApiModelProperty(notes = "is an array defining how many columns are being ordered upon - " +
            "i.e. if the array length is 1, then a single column sort is being performed," +
            " otherwise a multi-column sort is being performed")
    private List<@Valid Order> order = new ArrayList<>();

    @ApiModelProperty(notes = "Global search value. To be applied to all columns which have searchable as true")
    private @Valid Search search = new Search();

    public void validate() {
        if (!CollectionUtils.isEmpty(order) && order.get(0).getColumn() < columns.size()) {
            throw new ResourceAccessException("order column index: " + order.get(0).getColumn() + " exceeds column list size");
        }
    }

}

