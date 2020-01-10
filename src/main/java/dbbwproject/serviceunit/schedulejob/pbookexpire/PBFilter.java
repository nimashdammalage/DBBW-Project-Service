package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.PencilBooking;
import org.springframework.batch.item.ItemProcessor;

public class PBFilter implements ItemProcessor<PencilBooking, PencilBooking> {


    @Override
    public PencilBooking process(PencilBooking pb) {
//        Date currentDate = new Date();
//        if (pb.getPencilBookingStatus() != PencilBookingStatus.CUSTOMER_ARRIVED && pb.getMeetUpDate().before(currentDate)) {
//            return pb;
//        }
        return pb;
        //Returning null indicates that the item should not continue to be processed
    }
}
