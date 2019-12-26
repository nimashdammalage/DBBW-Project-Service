package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.dao.FBooking;
import dbbwproject.serviceunit.dao.FNotification;
import dbbwproject.serviceunit.dto.BookingDTO;
import dbbwproject.serviceunit.dto.NotificationDTO;
import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.dao.FPencilBooking;
import dbbwproject.serviceunit.pdfhandler.passport.DateOfBirth;
import dbbwproject.serviceunit.pdfhandler.passport.DualCitizenship;
import dbbwproject.serviceunit.pdfhandler.passport.PassportForm;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.*;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class ObjectMapperConfig {
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd";
    private static final String DATE_SPLITTER = "-";

    @Bean
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.validate(); // test that all fields are mapped

        //region PencilBookingDTO to FPencilBooking mappings
        PropertyMap<PencilBookingDTO, FPencilBooking> pm1 = new PropertyMap<PencilBookingDTO, FPencilBooking>() {
            @Override
            protected void configure() {
                using(pbdto -> ((PencilBookingDTO) pbdto.getSource()).getSeasonCode() + "_" + ((PencilBookingDTO) pbdto.getSource()).getTripCode())
                        .map(source, destination.getSeasonTripIndex());
            }
        };
        modelMapper.createTypeMap(PencilBookingDTO.class, FPencilBooking.class).addMappings(pm1);
        //endregion

        //region BookingDTO to FBooking mappings
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
        //endregion

        //region FNotification to NotificationDTO  mappings
        PropertyMap<FNotification, NotificationDTO> pm4 = new PropertyMap<FNotification, NotificationDTO>() {
            @Override
            protected void configure() {
                using(fNotify -> {
                            long createdDateLong = ((FNotification) fNotify.getSource()).getCreatedDate();
                            DateFormat df = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS);
                            return df.format(new Date(createdDateLong));
                        }
                )
                        .map(source, destination.getCreatedDate());
            }
        };
        modelMapper.createTypeMap(FNotification.class, NotificationDTO.class)
                .addMappings(pm4);
        //endregion

        //region FBooking to PassportForm mappings
        PropertyMap<BookingDTO, PassportForm> pm5 = new PropertyMap<BookingDTO, PassportForm>() {
            @Override
            protected void configure() {
                using(fNotify -> {
                            String dateOfBirth = ((BookingDTO) fNotify.getSource()).getDateOfBirth();
                            String[] splits = dateOfBirth.split(DATE_SPLITTER);
                            List<String> col = Arrays.stream(splits).filter(s -> !StringUtils.isBlank(s.trim())).collect(Collectors.toList());
                            return new DateOfBirth(col.get(2), col.get(1), col.get(0));
                        }
                )
                        .map(source, destination.getDateOfBirth());
            }
        };
        PropertyMap<BookingDTO, PassportForm> pm6 = new PropertyMap<BookingDTO, PassportForm>() {
            @Override
            protected void configure() {
                using(fNotify -> {
                            boolean dualCitizen = ((BookingDTO) fNotify.getSource()).isDualCitizen();
                            if (dualCitizen)
                                return DualCitizenship.YES;
                            return DualCitizenship.NO;
                        }
                )
                        .map(source, destination.getDualCitizen());
            }
        };
        modelMapper.createTypeMap(BookingDTO.class, PassportForm.class)
                .addMappings(pm5)
                .addMappings(pm6);

        //endregion

        //region String to long mappings
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
        //endregion

        return modelMapper;
    }
}
