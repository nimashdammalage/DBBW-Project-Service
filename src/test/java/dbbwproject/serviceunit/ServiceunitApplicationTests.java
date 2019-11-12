package dbbwproject.serviceunit;

import dbbwproject.serviceunit.controller.CustomerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServiceunitApplicationTests {
    @Autowired
    private CustomerController customerController;

    @Test
    public void contexLoads() throws Exception {
        assertThat(customerController).isNotNull();
    }

}
