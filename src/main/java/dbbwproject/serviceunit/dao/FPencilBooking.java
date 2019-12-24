package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.PencilBookingStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FPencilBooking {
    public static final String key = "pencil-bookings";
    public static final String SEASON_TRIP_INDEX = "seasonTripIndex";
    private String seasonCode;
    private String tripCode;
    private String personName;
    private String tpNo;
    private int seatCount;
    private String registrationNumbers;
    private long meetUpDate;
    private PencilBookingStatus pencilBookingStatus;
    private String seasonTripIndex;//combination of seasonCode_tripCode for indexing
}
