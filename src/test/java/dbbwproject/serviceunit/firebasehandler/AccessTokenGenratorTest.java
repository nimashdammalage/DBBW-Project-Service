package dbbwproject.serviceunit.firebasehandler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
class AccessTokenGenratorTest {
    private String path = "classpath:firebase_private_key.json";

    @Test
    void getAccessToken() throws IOException {
        String accessToken = AccessTokenGenrator.getAccessToken(path);
        System.out.println(accessToken);
    }
}