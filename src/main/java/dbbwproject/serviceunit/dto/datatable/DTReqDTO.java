package dbbwproject.serviceunit.dto.datatable;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DTReqDTO {
    public static final String lastCreatedTimeStamp = "lastCreatedTimeStamp";
    @PositiveOrZero(message = "draw value must be positive or zero")
    private int draw;

    @PositiveOrZero(message = "start value must be positive or zero")
    private int start;

    @Positive(message = "length value must be positive")
    private int length;

    @NotEmpty(message = "columns list cannot be empty.")
    private List<@Valid Column> columns = new ArrayList<>();

    @NotNull(message = "order list cannot be null.")
    @Size(min = 1, max = 1, message = "order list should have exactly one element")
    private List<@Valid Order> order = new ArrayList<>();

    private @Valid Search search = new Search();

    public void validate() {
        if (order.get(0).getColumn() >= columns.size()) {
            throw new IllegalArgumentException("order column index: " + order.get(0).getColumn() + " exceeds column list size");
        }
        Optional<Column> first = columns.stream().filter(c -> DTReqDTO.lastCreatedTimeStamp.equals(c.getData())).findFirst();
        if (first.isPresent()) {
            try {
                Long.parseLong(first.get().getName());
            } catch (Exception e) {
                throw new IllegalArgumentException("value of the column " + DTReqDTO.lastCreatedTimeStamp + " can not be converted to long");
            }
        }
    }
}
