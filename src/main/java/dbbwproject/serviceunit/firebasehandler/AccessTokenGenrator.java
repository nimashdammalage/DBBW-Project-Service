package dbbwproject.serviceunit.firebasehandler;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class AccessTokenGenrator {

    public static String getAccessToken(String keyPath) throws IOException {
        File file = ResourceUtils.getFile(keyPath);
        FileInputStream serviceAccount = new FileInputStream(file);
        GoogleCredential googleCred = GoogleCredential.fromStream(serviceAccount);
        GoogleCredential scoped = googleCred.createScoped(
                Arrays.asList(
                        "https://www.googleapis.com/auth/firebase.database",
                        "https://www.googleapis.com/auth/userinfo.email"
                )
        );
        scoped.refreshToken();
        return scoped.getAccessToken();
    }
}
