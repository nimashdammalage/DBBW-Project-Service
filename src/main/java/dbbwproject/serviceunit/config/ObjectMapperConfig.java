package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.firebasehandler.jsonobjects.FPencilBooking;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        //PencilBookingDTO to FPencilBooking mappings
        PropertyMap<PencilBookingDTO, FPencilBooking> pm1 = new PropertyMap<PencilBookingDTO, FPencilBooking>() {
            @Override
            protected void configure() {
                using(pbdto -> ((PencilBookingDTO) pbdto.getSource()).getSeasonCode() + "_" + ((PencilBookingDTO) pbdto.getSource()).getTripCode())
                        .map(source, destination.getTripSeasonIndex());
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(PencilBookingDTO.class, FPencilBooking.class).addMappings(pm1);
        modelMapper.validate(); // test that all fields are mapped
        return modelMapper;
    }
}
