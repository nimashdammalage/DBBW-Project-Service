package dbbwproject.serviceunit.firebasehandler.jsonobjects;

import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.dto.PencilBookingStatus;
import lombok.*;

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
    private String meetUpDate;
    private PencilBookingStatus pencilBookingStatus;
    private String tripSeasonIndex;//combination of seasonCode_tripCode for indexing
}
