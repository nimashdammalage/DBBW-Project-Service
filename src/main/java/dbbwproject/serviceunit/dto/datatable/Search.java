package dbbwproject.serviceunit.dto.datatable;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Search {
    @NotNull(message = "value must not be null")
    private String value;

    private boolean regex;
}
