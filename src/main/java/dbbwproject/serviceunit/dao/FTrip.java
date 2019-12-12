package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.TripStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FTrip {
    private String code;
    private String seasonCode;
    private int perPersonCost;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate ppColDate;
    private int passengerCount;
    private TripStatus tripStatus;
}
