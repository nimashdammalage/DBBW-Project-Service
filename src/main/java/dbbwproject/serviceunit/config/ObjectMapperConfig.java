package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.dao.FBooking;
import dbbwproject.serviceunit.dto.BookingDTO;
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
    private static final String YYYY_MM_DD_T_HH_MM_SS_MILSEC = "yyyy-MM-dd'T'HH:mm:ss.SSS";

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
                        .map(source, destination.getSeasonTripIndex());
            }
        };
        modelMapper.createTypeMap(PencilBookingDTO.class, FPencilBooking.class).addMappings(pm1);

        //BookingDTO to FBooking mappings
        PropertyMap<BookingDTO, FBooking> pm2 = new PropertyMap<BookingDTO, FBooking>() {
            @Override
            protected void configure() {
                using(pbdto -> ((BookingDTO) pbdto.getSource()).getSeasonCode() + "_" + ((BookingDTO) pbdto.getSource()).getTripCode())
                        .map(source, destination.getSeasonTripIndex());
            }
        };
        PropertyMap<BookingDTO, FBooking> pm3 = new PropertyMap<BookingDTO, FBooking>() {
            @Override
            protected void configure() {
                using(pbdto -> ((BookingDTO) pbdto.getSource()).getSeasonCode() + "_" + ((BookingDTO) pbdto.getSource()).getTripCode() + "_" + ((BookingDTO) pbdto.getSource()).getPbPersonName())
                        .map(source, destination.getSeasonTripPNameIndex());
            }
        };
        modelMapper.createTypeMap(BookingDTO.class, FBooking.class)
                .addMappings(pm2)
                .addMappings(pm3);

        //String to long mappings
        Converter<String, Long> toStringDate = new AbstractConverter<String, Long>() {
            @Override
            protected Long convert(String source) {
                DateFormat df = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_MILSEC);
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
                DateFormat df = new SimpleDateFormat(YYYY_MM_DD_T_HH_MM_SS_MILSEC);
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
