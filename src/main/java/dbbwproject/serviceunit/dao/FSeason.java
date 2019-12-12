package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.SeasonStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FSeason {
    private String code;
    private SeasonStatus status;
}
