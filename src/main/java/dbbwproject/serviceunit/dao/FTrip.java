package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.TripStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FTrip {
    private String code;
    private String seasonCode;
    private int perPersonCost;
    private String startDate;
    private String endDate;
    private String ppColDate;
    private int passengerCount;
    private TripStatus tripStatus;
}
