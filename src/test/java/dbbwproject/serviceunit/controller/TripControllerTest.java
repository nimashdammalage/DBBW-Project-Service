package dbbwproject.serviceunit.controller;

import dbbwproject.serviceunit.config.FireBaseAppConfig;
import dbbwproject.serviceunit.config.FirebaseAuthAndDBConfig;
import dbbwproject.serviceunit.config.ObjectMapperConfig;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({FirebaseAuthAndDBConfig.class, FireBaseAppConfig.class, ObjectMapperConfig.class})
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {TripController.class, SeasonController.class})
class TripControllerTest {
    SeasonDTO s1 = new SeasonDTO("ss1", SeasonStatus.CURRENT);
    SeasonDTO sComplete = new SeasonDTO("sCompleteCode", SeasonStatus.COMPLETED);
    TripDTO trip1 = new TripDTO("trip1", s1.getCode(), 500, "2018-01-13", "2019-02-19", "2020-10-15", 150, TripStatus.COMPLETED);
    TripDTO tComplete = new TripDTO("trip1", sComplete.getCode(), 500, "2018-01-13", "2019-02-19", "2020-10-15", 150, TripStatus.COMPLETED);


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTripStatus() throws Exception {
        mockMvc.perform(
                get("/resource-management/seasons/trips/trip-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("WORKING"))
                .andExpect(jsonPath("$[1]").value("COMPLETED"));
    }

    @Test
    public void testTripBookedSeats() throws Exception {
        mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}/booked-seat-numbers","ss1","trip1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSeasonPost() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons")
                        .content(asJsonString(s1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripPOSTErr() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", "6rdCode")
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTripPOSTNoSeason() throws Exception {
        TripDTO trip1 = new TripDTO("trip1", "aaa", 500, "2018-01-13", "2019-02-19", "2020-10-15", 150, TripStatus.COMPLETED);
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", "aaa")
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTripPOST() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", s1.getCode())
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripPOSTAlreadyErr() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", s1.getCode())
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSeasonCompletedErr() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons")
                        .content(asJsonString(sComplete))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripPOSTSCompleteErr() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", tComplete.getSeasonCode())
                        .content(asJsonString(tComplete))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTripGETbySeason() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips", trip1.getSeasonCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    public void testTripGETbySeasonNoData() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips", "abder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }


    @Test
    public void testTripGETbySeasonAndTrip() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}", trip1.getSeasonCode(),trip1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    public void testTripDelete() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{seasonCode}/trips/{tripCode}", trip1.getSeasonCode(), trip1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripDeleteDupErr() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{seasonCode}/trips/{tripCode}", trip1.getSeasonCode(), trip1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTripPOSTSecond() throws Exception {
        mockMvc.perform(
                post("/resource-management/seasons/{seasonCode}/trips", s1.getCode())
                        .content(asJsonString(trip1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testTripDeletePBExist() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{seasonCode}/trips/{tripCode}", trip1.getSeasonCode(), trip1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}