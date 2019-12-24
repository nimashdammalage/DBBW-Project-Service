package dbbwproject.serviceunit.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseAppConfig {
    @Value("${firebase.service-account-filename}")
    private Resource serviceAccountKeyResource;

    @Value("${firebase.realtime-database-url}")
    private String url;

    @Bean
    FirebaseApp createFireBaseApp() throws IOException {
        InputStream inputStream = serviceAccountKeyResource.getInputStream();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl(url)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}