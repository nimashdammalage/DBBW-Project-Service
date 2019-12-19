package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.dao.FPencilBooking;
import org.modelmapper.*;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Configuration
public class ObjectMapperConfig {
    static final String YYYY_MM_DD = "yyyy-MM-dd";

    @Bean
    public ModelMapper createModelMapper() {
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

        //String to long mappings
        Converter<String, Long> toStringDate = new AbstractConverter<String, Long>() {
            @Override
            protected Long convert(String source) {
                DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
                try {
                    return df.parse(source).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                throw new IllegalStateException("failed date conversion. model mapper object");
            }
        };
        modelMapper.createTypeMap(String.class, Long.class);
        modelMapper.addConverter(toStringDate);

        Converter<Long, String> toLongDate = new AbstractConverter<Long, String>() {
            @Override
            protected String convert(Long dateLong) {
                DateFormat df = new SimpleDateFormat(YYYY_MM_DD);
                try {
                    return df.format(new Date(dateLong));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw new IllegalStateException("failed date conversion. model mapper object");
            }
        };
        modelMapper.createTypeMap(Long.class, String.class);
        modelMapper.addConverter(toLongDate);

        return modelMapper;
    }
}
