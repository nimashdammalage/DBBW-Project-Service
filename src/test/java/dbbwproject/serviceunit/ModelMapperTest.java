package dbbwproject.serviceunit;

import dbbwproject.serviceunit.config.FireBaseAppConfig;
import dbbwproject.serviceunit.config.FirebaseAuthAndDBConfig;
import dbbwproject.serviceunit.config.ObjectMapperConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({ObjectMapperConfig.class})
@RunWith(SpringRunner.class)
public class ModelMapperTest {

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void test() {
//        String dateTest = "2000-09-27";
//        LocalDate dateConverted = modelMapper.map(dateTest, LocalDate.class);
//        System.out.println("output: \n\n");
//        assertEquals(dateConverted.toString(),"2000-09-27");

        LocalDate localDate = LocalDate.of(2018, 2, 15);
        Date date = new Date();
        assertEquals(modelMapper.map(date.getTime(), String.class),"2019-12-21T13:10:26.641");
    }
}
