package dbbwproject.serviceunit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({FirebaseAuthAndDBConfig.class, FireBaseAppConfig.class, ObjectMapperConfig.class})
@RunWith(SpringRunner.class)
@WebMvcTest(SeasonController.class)
class SeasonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    SeasonDTO s1 = new SeasonDTO("c4", SeasonStatus.CURRENT);
    SeasonDTO s3 = new SeasonDTO("c2", SeasonStatus.COMPLETED);

    @Test
    public void testSeasonStatus() throws Exception {
        SeasonDTO ss = new SeasonDTO(null, SeasonStatus.COMPLETED);

        mockMvc.perform(
                get("/resource-management/seasons/season-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("COMPLETED"))
                .andExpect(jsonPath("$[1]").value("UP_COMING"))
                .andExpect(jsonPath("$[2]").value("CURRENT"));
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
    public void testSeasonPostDuplicateErr() throws Exception {
        SeasonDTO s2 = new SeasonDTO(s1.getCode(), SeasonStatus.COMPLETED);
        mockMvc.perform(
                post("/resource-management/seasons")
                        .content(asJsonString(s2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSeasonPost3() throws Exception {

        mockMvc.perform(
                post("/resource-management/seasons")
                        .content(asJsonString(s3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSeasonPostTwoCurrentErr() throws Exception {
        SeasonDTO s4 = new SeasonDTO("c3", SeasonStatus.CURRENT);
        mockMvc.perform(
                post("/resource-management/seasons")
                        .content(asJsonString(s4))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testSeasonGETByCode() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{code}", s1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(s1.getCode()))
                .andExpect(jsonPath("$.status").value(s1.getStatus().toString())));
    }

    @Test
    public void testSeasonGET() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk()));
    }

    @Test
    public void testSeasonputErr() throws Exception {
        SeasonDTO ss = new SeasonDTO("6rdCode", SeasonStatus.CURRENT);
        mockMvc.perform(
                put("/resource-management/seasons/{code}", "abndse")
                        .content(asJsonString(ss))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSeasonputCurrErr() throws Exception {
        SeasonDTO ss = new SeasonDTO(s3.getCode(), SeasonStatus.CURRENT);

        mockMvc.perform(
                put("/resource-management/seasons/{code}", s3.getCode())
                        .content(asJsonString(ss))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSeasonput() throws Exception {
        SeasonDTO ss = new SeasonDTO(s1.getCode(), SeasonStatus.UP_COMING);

        mockMvc.perform(
                put("/resource-management/seasons/{code}", s1.getCode())
                        .content(asJsonString(ss))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSeasonDelete() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{code}", s1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSeasonDeleteErr() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{code}", s1.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSeasonDeleteWithTrips2() throws Exception {
        mockMvc.perform(
                delete("/resource-management/seasons/{code}", s3.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}