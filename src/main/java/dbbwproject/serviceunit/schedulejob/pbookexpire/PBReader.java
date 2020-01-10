package dbbwproject.serviceunit.schedulejob.pbookexpire;

import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dto.PencilBookingStatus;
import dbbwproject.serviceunit.dto.TripStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;

import java.util.List;

public class PBReader implements ItemReader<PencilBooking> {
    private ItemReader<PencilBooking> delegate;
    private final SessionFactory sessionFactory;

    public PBReader(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PencilBooking read() throws Exception {
        List<PencilBooking> fBokList = getList();
        System.out.println("Item reader met");
        if (delegate == null) {
            delegate = new IteratorItemReader<>(fBokList);
        }
        return delegate.read();
    }

    private List<PencilBooking> getList() {
        try (Session session = sessionFactory.openSession()) {
            Query<PencilBooking> query = session.createQuery("From PencilBooking p " +
                    "where p.trip.tripStatus != ?1 " +
                    "and p.meetUpDate >= current_date " +
                    "and p.pencilBookingStatus = ?2", PencilBooking.class)
                    .setParameter(1, TripStatus.COMPLETED)
                    .setParameter(2, PencilBookingStatus.CUSTOMER_NOT_ARRIVED);
            return query.list();
        }
    }
}
