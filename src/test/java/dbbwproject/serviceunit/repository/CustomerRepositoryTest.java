package dbbwproject.serviceunit.repository;

import dbbwproject.serviceunit.ServiceunitApplication;
import dbbwproject.serviceunit.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceunitApplication.class})
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void getAll() {
        Iterable<Customer> all = customerRepository.findAll();
        System.out.println("success");

        Customer customer = new Customer();
        customer.setName("nimash");
        customer.setNicNumber("932361256v");
        customer.setEmail("myemail@gmail.com");
        Customer save = customerRepository.save(customer);

        Optional<Customer> byId = customerRepository.findById(1L);

    }
}