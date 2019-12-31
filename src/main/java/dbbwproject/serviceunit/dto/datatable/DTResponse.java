package dbbwproject.serviceunit.dto.datatable;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DTResponse<T> {
    private List<T> data;
    private int draw;
    private int recordsFiltered;
    private int recordsTotal;
}
