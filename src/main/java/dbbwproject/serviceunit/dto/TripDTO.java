package dbbwproject.serviceunit.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TripDTO {
    private int code;
    private int perPersonCost;
    private Date startDate;
    private Date endDate;
    private Date ppColDate;
    private String seasonCode;
}
