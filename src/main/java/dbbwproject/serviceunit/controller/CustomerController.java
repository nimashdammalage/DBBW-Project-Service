package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.entity.Customer;
import dbbwproject.serviceunit.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {
    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @PostMapping("/customers")
    void addUser(@RequestBody Customer customer) {
        customerRepository.save(customer);
    }
}
