package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
