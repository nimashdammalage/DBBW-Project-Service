package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dto.PencilBookingStatus;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;


public class PBFilter implements ItemProcessor<FPencilBooking, FPencilBooking> {


    @Override
    public FPencilBooking process(FPencilBooking fPencilBooking) {
        Date currentDate = new Date();
        if (fPencilBooking.getPencilBookingStatus() != PencilBookingStatus.CUSTOMER_ARRIVED
                && fPencilBooking.getMeetUpDate() < currentDate.getTime()) {
            return fPencilBooking;
        }
        return null;
        //Returning null indicates that the item should not continue to be processed
    }
}
