package dbbwproject.serviceunit;

import dbbwproject.serviceunit.controller.SeasonController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServiceunitApplicationTests {

    @Autowired
    SeasonController seasonController;

    @Test
    void contexLoads() {
        assertThat(seasonController).isNotNull();
    }

}
