package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.TripStatus;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FTrip {
    public static final String key = "trips";
    public static final String SEASON_CODE = "seasonCode";
    public static final String TRIP_STATUS = "tripStatus";
    private String code;
    private String seasonCode;
    private int perPersonCost;
    private long startDate;
    private long endDate;
    private long ppColDate;
    private int passengerCount;
    private TripStatus tripStatus;
}
