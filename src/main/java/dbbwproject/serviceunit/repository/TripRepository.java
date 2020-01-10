package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.dao.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Integer> {
}
