package dbbwproject.serviceunit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dbbwproject.serviceunit.dto.SeasonDto;
import dbbwproject.serviceunit.dto.SeasonStatus;
import dbbwproject.serviceunit.dto.TripDto;
import dbbwproject.serviceunit.dto.TripStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {TripController.class, SeasonController.class})
class TripControllerTest {
    private final SeasonDto s1 = new SeasonDto("ss1", SeasonStatus.CURRENT);
    private final SeasonDto sComplete = new SeasonDto("sCompleteCode", SeasonStatus.COMPLETED);
    private final TripDto trip1 = new TripDto("trip1", s1.getCode(), 500, "2018-01-13", "2019-02-19", "2020-10-15", 150, TripStatus.COMPLETED);
    private final TripDto tComplete = new TripDto("trip1", sComplete.getCode(), 500, "2018-01-13", "2019-02-19", "2020-10-15", 150, TripStatus.COMPLETED);


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
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}/booked-seat-numbers", "ss1", "trip1")
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
        TripDto trip1 = new TripDto("trip1", "aaa", 500, "2018-01-13", "2019-02-19", "2020-10-15", 150, TripStatus.COMPLETED);
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
                get("/resource-management/seasons/{seasonCode}/trips/{tripCode}", trip1.getSeasonCode(), trip1.getCode())
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}