package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.dao.FPencilBooking;
import org.modelmapper.*;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.validate(); // test that all fields are mapped

        //PencilBookingDTO to FPencilBooking mappings
        PropertyMap<PencilBookingDTO, FPencilBooking> pm1 = new PropertyMap<PencilBookingDTO, FPencilBooking>() {
            @Override
            protected void configure() {
                using(pbdto -> ((PencilBookingDTO) pbdto.getSource()).getSeasonCode() + "_" + ((PencilBookingDTO) pbdto.getSource()).getTripCode())
                        .map(source, destination.getTripSeasonIndex());
            }
        };
        modelMapper.createTypeMap(PencilBookingDTO.class, FPencilBooking.class).addMappings(pm1);

        //String to LocalDate mappings
        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now();
            }
        };
        Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(source, format);
            }
        };
        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);



        return modelMapper;
    }
}
