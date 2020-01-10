package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.dbutil.DBUtil;
import dbbwproject.serviceunit.mapper.*;
import dbbwproject.serviceunit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {


    @Autowired
    public ServiceConfig(SeasonRepository seasonRepository, TripRepository tripRepository, PencilBookingRepository pencilBookingRepository, BookingRepository bookingRepository, SettingsRepository settingsRepository, NotificationRepository notificationRepository, SeasonMapperImpl sm, TripMapperImpl tm, PencilBookingMapperImpl pm, BookingMapperImpl bm, SettingsMapperImpl stm, NotificationMapperImpl nm, DBUtil dbUtil) {
    }

//    @Bean
//    public SeasonService createSeasonService() {
//        return new SeasonService(seasonRepository, sm, dbUtil);
//    }
//
//    @Bean
//    public TripService createTripService() {
//        return new TripService(tripRepository, tm, dbUtil);
//    }
//
//    @Bean
//    public PencilBookingService createPencilBookingService() {
//        return new PencilBookingService(pencilBookingRepository, pm, dbUtil, createSettingService());
//    }
//
//    @Bean
//    public SettingService createSettingService() {
//        return new SettingService(settingsRepository, stm);
//    }
//
//    @Bean
//    public BookingService createBookingService() {
//        return new BookingService(bookingRepository, bm, dbUtil);
//    }
//
//    @Bean
//    public NotificationService createNotificationService() {
//        return new NotificationService(notificationRepository, nm, dbUtil);
//    }

    @Bean
    public DateMapper createDateMapper() {
        return new DateMapper();
    }


}
