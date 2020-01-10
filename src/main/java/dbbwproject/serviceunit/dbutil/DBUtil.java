package dbbwproject.serviceunit.dbutil;

import dbbwproject.serviceunit.dao.*;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Component
public class DBUtil {
    private SessionFactory sm;

    @Autowired
    public DBUtil(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sm = entityManagerFactory.unwrap(SessionFactory.class);
    }

    public SessionFactory getSm() {
        return sm;
    }

    public List<Season> getSeasons(int fIndex, int size) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Season> query = session.createQuery("From Season order by createdTimestamp desc", Season.class);
        query.setFirstResult(fIndex);
        query.setMaxResults(size);
        List<Season> seasons = query.getResultList();
        session.getTransaction().commit();
        return seasons;
    }

    public List<Season> getSeasons() {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Season> query = session.createQuery("From Season order by createdTimestamp desc", Season.class);
        List<Season> seasons = query.getResultList();
        session.getTransaction().commit();
        return seasons;
    }

    public Season getSeason(String seasonCode) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Season> query = session.createQuery("From Season s where s.code = ?1", Season.class)
                .setParameter(1, seasonCode);
        Season season = query.getResultList().stream().findFirst().orElse(null);
        session.getTransaction().commit();
        return season;
    }

    public List<Trip> getTrips(String seasonCode) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Trip> query = session.createQuery("From Trip t where t.season.code = ?1", Trip.class)
                .setParameter(1, seasonCode);
        List<Trip> trip = query.getResultList();
        session.getTransaction().commit();
        return trip;
    }

    public List<Trip> getTrips(String seasonCode, int fIndex, int size) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Trip> query = session.createQuery("From Trip t where t.season.code = ?1 order by createdTimestamp desc", Trip.class)
                .setParameter(1, seasonCode);
        query.setFirstResult(fIndex);
        query.setMaxResults(size);
        List<Trip> trips = query.getResultList();
        session.getTransaction().commit();
        return trips;
    }

    public Trip getTrip(String seasonCode, String tripCode) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Trip> query = session.createQuery("From Trip t where t.season.code = ?1 and t.code = ?2", Trip.class)
                .setParameter(1, seasonCode)
                .setParameter(2, tripCode);
        Trip trip = query.getResultList().stream().findFirst().orElse(null);
        session.getTransaction().commit();
        return trip;
    }

    public PencilBooking getPencilBooking(String seasonCode, String tripCode, String personName) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<PencilBooking> query = session.createQuery("From PencilBooking p  where p.personName = ?1 and p.trip.code = ?2 and p.trip.season.code = ?3", PencilBooking.class)
                .setParameter(1, personName)
                .setParameter(2, tripCode)
                .setParameter(3, seasonCode);
        PencilBooking pb = query.getResultList().stream().findFirst().orElse(null);
        session.getTransaction().commit();
        return pb;
    }

    public Booking getBooking(String seasonCode, String tripCode, int regNumber) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Booking> query = session.createQuery("From Booking b where b.registrationNumber.regNumber = ?1 and b.registrationNumber.trip.code = ?2 and b.registrationNumber.trip.season.code = ?3", Booking.class)
                .setParameter(1, regNumber)
                .setParameter(2, tripCode)
                .setParameter(3, seasonCode);
        Booking book = query.getResultList().stream().findFirst().orElse(null);
        session.getTransaction().commit();
        return book;
    }

    public List<Booking> getBookingsForTrip(String seasonCode, String tripCode) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Booking> query = session.createQuery("From Booking b where b.registrationNumber.trip.code = ?1 and b.registrationNumber.trip.season.code = ?2", Booking.class)
                .setParameter(1, tripCode)
                .setParameter(2, seasonCode);
        return query.getResultList();
    }

    public List<Booking> getBookingsForTrip(String seasonCode, String tripCode, int fIndex, int size) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Booking> query = session.createQuery("From Booking b " +
                "where b.registrationNumber.trip.code = ?1 and b.registrationNumber.trip.season.code = ?2 " +
                "order by createdTimestamp desc", Booking.class)
                .setParameter(1, tripCode)
                .setParameter(2, seasonCode);
        query.setFirstResult(fIndex);
        query.setMaxResults(size);
        List<Booking> bookings = query.getResultList();
        session.getTransaction().commit();
        return bookings;
    }

    public List<PencilBooking> getPencilBookings(String seasonCode, String tripCode) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<PencilBooking> query = session.createQuery("From PencilBooking p  where p.trip.code = ?1 and p.trip.season.code = ?2", PencilBooking.class)
                .setParameter(1, tripCode)
                .setParameter(2, seasonCode);
        List<PencilBooking> pbs = query.getResultList();
        session.getTransaction().commit();
        return pbs;
    }

    public List<PencilBooking> getPencilBookings(String seasonCode, String tripCode, int fIndex, int size) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<PencilBooking> query = session.createQuery("From PencilBooking p  where p.trip.code = ?1 and p.trip.season.code = ?2 order by p.createdTimestamp desc", PencilBooking.class)
                .setParameter(1, tripCode)
                .setParameter(2, seasonCode);
        query.setFirstResult(fIndex);
        query.setMaxResults(size);
        List<PencilBooking> pbs = query.getResultList();
        session.getTransaction().commit();
        return pbs;
    }

    public List<Notification> getNotifications(int fIndex, int size) {
        Session session = sm.getCurrentSession();
        session.beginTransaction();
        Query<Notification> query = session.createQuery("From Notification order by createdDate desc", Notification.class);
        query.setFirstResult(fIndex);
        query.setMaxResults(size);
        List<Notification> nots = query.getResultList();
        session.getTransaction().commit();
        return nots;
    }
}
