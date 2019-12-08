package dbbwproject.serviceunit.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PencilBookingDTO {
    private String name;
    private int tpNo;
    private int seatCount;
    private Date meetupDate;
}
