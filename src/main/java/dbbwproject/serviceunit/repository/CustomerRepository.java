package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
