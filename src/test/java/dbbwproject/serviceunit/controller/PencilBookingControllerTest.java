package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.config.FireBaseAppConfig;
import dbbwproject.serviceunit.config.FirebaseAuthAndDBConfig;
import dbbwproject.serviceunit.config.ObjectMapperConfig;
import dbbwproject.serviceunit.dto.PencilBookingDTO;
import dbbwproject.serviceunit.dto.PencilBookingStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static dbbwproject.serviceunit.controller.SeasonControllerTest.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({FirebaseAuthAndDBConfig.class, FireBaseAppConfig.class, ObjectMapperConfig.class})
@RunWith(SpringRunner.class)
@WebMvcTest(PencilBookingController.class)
class PencilBookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPencilBookingPOST() throws Exception {
        PencilBookingDTO pb = new PencilBookingDTO("season2", "trip12", "Nadeesha", "771650539", 5, "2018-11-24", PencilBookingStatus.CUSTOMER_NOT_ARRIVED);
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips/{tripCode}/pencil-bookings", "season2", "trip12")
                        .content(asJsonString(pb))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPencilBookingGET() throws Exception {
        mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}", "season2", "trip12", "Nadeesha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPencilBookingGETbyTrip() throws Exception {
        mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}/pencil-bookings", "season2", "trip12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testPencilBookingPut() throws Exception {
        PencilBookingDTO pb = new PencilBookingDTO("season2", "trip12", "Nadeesha", "777777", 555, "2018-11-24", PencilBookingStatus.CUSTOMER_NOT_ARRIVED);
        mockMvc.perform(
                put("/resource-management/seasons/{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}", "season2", "trip12", "Nadeesha")
                        .content(asJsonString(pb))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripDelete() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{seasonCode}/trips/{tripCode}/pencil-bookings/{personName}", "season2", "trip12", "Nadeesha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

}