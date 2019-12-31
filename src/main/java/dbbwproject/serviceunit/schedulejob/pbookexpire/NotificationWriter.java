package dbbwproject.serviceunit.schedulejob.pbookexpire;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import dbbwproject.serviceunit.dao.FNotification;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public class NotificationWriter implements ItemWriter<FPencilBooking> {
    private final DatabaseReference dbRef;
    private final ModelMapper modelMapper;
    private String jobCode;

    NotificationWriter(ModelMapper modelMapper) {
        dbRef = FirebaseDatabase.getInstance().getReference("resources");
        this.modelMapper = modelMapper;
    }

    @Value("#{jobParameters['jobCode']}")
    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    @Override
    public void write(List<? extends FPencilBooking> fPencilBookings) {

        int i = 1;
        System.out.println("ItemWriter met");
        for (int j = 0; j < fPencilBookings.size(); j++) {
            FPencilBooking fPencilBooking = fPencilBookings.get(j);
            PencilBookingDTO mappedDTO = modelMapper.map(fPencilBooking, PencilBookingDTO.class);
            String notId = jobCode + "_" + mappedDTO.getSeasonCode() + "_" + mappedDTO.getTripCode() + "_" + mappedDTO.getPersonName() + j;
            String message = "Person: " + fPencilBooking.getPersonName() + " from trip: " + fPencilBooking.getTripCode() + " and season: " + fPencilBooking.getSeasonCode()
                    + " has not come to office before meet up date: " + mappedDTO.getMeetUpDate() + " .his TP no: " + mappedDTO.getTpNo();
            FNotification fnotification = new FNotification(notId, new Date().getTime() * -1, message, false);

            System.out.println("inserting FNotification: " + notId + " to DB");
            DBHandle.insertDataToDB(fnotification, dbRef.child(FNotification.key).child(notId));
        }
    }
}
