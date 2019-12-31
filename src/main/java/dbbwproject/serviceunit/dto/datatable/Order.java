package dbbwproject.serviceunit.dto.datatable;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @PositiveOrZero(message = "column value must be positive or zero")
    private int column;

    private Dir dir;
}
