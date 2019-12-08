package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.config.FireBaseAppConfig;
import dbbwproject.serviceunit.config.FirebaseAuthAndDBConfig;
import dbbwproject.serviceunit.dto.TripDTO;
import dbbwproject.serviceunit.dto.TripStatus;
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

@Import({FirebaseAuthAndDBConfig.class, FireBaseAppConfig.class})
@RunWith(SpringRunner.class)
@WebMvcTest(TripController.class)
class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTripGETbySeason() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips", "6rdCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    public void testTripPOST() throws Exception {
        TripDTO trip1 = new TripDTO("trip1", "6rdCode", 500, "2018-01-13", "2019-02-19", "2020-10-15", TripStatus.COMPLETED);
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", "6rdCode")
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void testTripGETbySeasonAndTrip() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}", "3rdCode","trip1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    public void testTripput() throws Exception {
        TripDTO trip1 = new TripDTO("trip1", "6rdCode", 1000, "2018-01-14", "2019-02-14", "2020-10-14", TripStatus.WORKING);
        mockMvc.perform(
                put("/resource-management/seasons/{seasonCode}/trips/{tripCode}", "6rdCode","trip1")
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripDelete() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{seasonCode}/trips/{tripCode}", "6rdCode","trip1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

}