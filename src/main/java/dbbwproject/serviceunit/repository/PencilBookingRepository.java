package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.PencilBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PencilBookingRepository extends JpaRepository<PencilBooking, Integer> {
}
