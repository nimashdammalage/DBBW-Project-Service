package dbbwproject.serviceunit.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
class QueryParam<T> {

    private final String code;
    private final boolean wildCard;
    private final String queryField;
    private T queryValue;
    private boolean searchable;
    private boolean orderly;
}
