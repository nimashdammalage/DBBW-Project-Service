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
    private String seasonCode;
    private String tripCode;
    private String personName;
    private String tpNo;
    private int seatCount;
    private LocalDate meetUpDate;
    private PencilBookingStatus pencilBookingStatus;
    private String tripSeasonIndex;//combination of seasonCode_tripCode for indexing
}
