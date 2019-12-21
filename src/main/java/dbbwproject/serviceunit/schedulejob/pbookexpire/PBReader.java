package dbbwproject.serviceunit.schedulejob.pbookexpire;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.dao.FTrip;
import dbbwproject.serviceunit.dto.TripStatus;
import dbbwproject.serviceunit.firebasehandler.DBHandle;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class PBReader implements ItemReader<FPencilBooking> {
    private ItemReader<FPencilBooking> delegate;
    private final DatabaseReference dbRef;

    PBReader() {
        dbRef = FirebaseDatabase.getInstance().getReference("resources");
    }

    @Override
    public FPencilBooking read() throws Exception {
        List<FPencilBooking> fBokList = getList();
        System.out.println("Item reader met");
        if (delegate == null) {
            delegate = new IteratorItemReader<>(fBokList);
        }
        return delegate.read();
    }

    private List<FPencilBooking> getList() {
        Query workingTripsQuery = dbRef.child(FTrip.key).orderByChild(FTrip.TRIP_STATUS).equalTo(TripStatus.WORKING.toString());
        ResponseEntity<List<FTrip>> workingTrips = DBHandle.retrieveDataList(FTrip.class, workingTripsQuery);

        List<FPencilBooking> resultPBList = new ArrayList<>();
        if (workingTrips.getBody() == null || workingTrips.getBody().isEmpty()) {
            return null;
        }

        for (FTrip fTrip : workingTrips.getBody()) {
            String key = fTrip.getSeasonCode() + "_" + fTrip.getCode();
            Query pbQuery = dbRef.child(FPencilBooking.key).orderByChild(FPencilBooking.TRIP_SEASON_INDEX).equalTo(key);
            ResponseEntity<List<FPencilBooking>> pBookings = DBHandle.retrieveDataList(FPencilBooking.class, pbQuery);
            if (pBookings.getBody() != null && !pBookings.getBody().isEmpty()) {
                resultPBList.addAll(pBookings.getBody());
            }
        }
        return resultPBList;
    }
}
