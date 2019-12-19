package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.config.FireBaseAppConfig;
import dbbwproject.serviceunit.config.FirebaseAuthAndDBConfig;
import dbbwproject.serviceunit.config.ObjectMapperConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({FirebaseAuthAndDBConfig.class, FireBaseAppConfig.class, ObjectMapperConfig.class})
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { NotificationController.class})
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetAllNotifications() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/notifications")
                        .param("unreadOnly","false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    public void testGetNotificationsUnread() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/notifications")
                        .param("unreadOnly","true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

}