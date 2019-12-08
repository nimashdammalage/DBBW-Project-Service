package dbbwproject.serviceunit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import dbbwproject.serviceunit.controller.SeasonController;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServiceunitApplicationTests {

    @Autowired
    SeasonController seasonController;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FirebaseAuth firebaseAuth;

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Test
    void contexLoads() {
        assertThat(seasonController).isNotNull();
    }

}
