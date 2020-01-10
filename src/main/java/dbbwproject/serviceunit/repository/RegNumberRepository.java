package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.PencilBooking;
import dbbwproject.serviceunit.dao.RegNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegNumberRepository extends JpaRepository<RegNumber, Integer> {
    Optional<RegNumber> findRegNumberByPencilBookingAndRegNumber(PencilBooking pb, Integer regNum);
}
