package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.SeasonStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FSeason {
    public static final String key = "seasons";
    public static final String STATUS = "status";
    private String code;
    private SeasonStatus status;
}
