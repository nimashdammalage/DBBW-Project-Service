package dbbwproject.serviceunit.config;

import com.google.firebase.FirebaseApp;
import dbbwproject.serviceunit.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    private final ModelMapper modelMapper;
    private final FirebaseApp firebaseApp;

    @Autowired
    public ServiceConfig(ModelMapper modelMapper, FirebaseApp firebaseApp) {
        this.modelMapper = modelMapper;
        this.firebaseApp = firebaseApp;
    }

    @Bean
    public SeasonService createSeasonService() {
        return new SeasonService(modelMapper, firebaseApp);
    }

    @Bean
    public TripService createTripService() {
        return new TripService(modelMapper, firebaseApp);
    }

    @Bean
    public PencilBookingService createPencilBookingService() {
        return new PencilBookingService(modelMapper, firebaseApp);
    }

    @Bean
    public SettingService createSettingService() {
        return new SettingService(modelMapper, firebaseApp);
    }

    @Bean
    public BookingService createBookingService() {
        return new BookingService(modelMapper, firebaseApp);
    }

    @Bean
    public NotificationService createNotificationService() {
        return new NotificationService(modelMapper, firebaseApp);
    }

}
