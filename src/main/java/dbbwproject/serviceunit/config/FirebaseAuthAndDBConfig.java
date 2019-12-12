package dbbwproject.serviceunit.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class FirebaseAuthAndDBConfig {
    private FirebaseApp firebaseApp;

    @Autowired
    public FirebaseAuthAndDBConfig(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
    }

    @Bean
    FirebaseAuth createFirebaseAuth() {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
