package dbbwproject.serviceunit.config;

import dbbwproject.serviceunit.settings.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettingsConfig {
    @Bean
    public Settings createSettings() {
        return Settings.getInstance();
    }
}
