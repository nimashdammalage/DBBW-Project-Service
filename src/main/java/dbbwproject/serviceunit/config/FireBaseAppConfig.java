package dbbwproject.serviceunit.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FireBaseAppConfig {
    @Value("${firebase.service-account-filename}")
    private String serviceAccountKeyPath;

    @Value("${firebase.realtime-database-url}")
    private String url;

    @Bean
    FirebaseApp createFireBaseApp() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(ResourceUtils.getFile(serviceAccountKeyPath));
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(url)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}