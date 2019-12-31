package dbbwproject.serviceunit.dto.datatable;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Column {
    @NotBlank(message = "data value must not be blank")
    private String data;
    @NotNull(message = "name value must not be null")
    private String name;
    private boolean searchable;
    private boolean orderable;
    @NotNull(message = "search value must not be null")
    private @Valid Search search = new Search();
}
