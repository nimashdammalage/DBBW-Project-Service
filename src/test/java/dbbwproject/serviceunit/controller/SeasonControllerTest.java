package dbbwproject.serviceunit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dbbwproject.serviceunit.config.FireBaseAppConfig;
import dbbwproject.serviceunit.config.FirebaseAuthAndDBConfig;
import dbbwproject.serviceunit.config.ModelMapperConfig;
import dbbwproject.serviceunit.dto.SeasonDTO;
import dbbwproject.serviceunit.dto.SeasonStatus;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({FirebaseAuthAndDBConfig.class, FireBaseAppConfig.class, ModelMapperConfig.class})
@RunWith(SpringRunner.class)
@WebMvcTest(SeasonController.class)
class SeasonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSeasonPost() throws Exception {
        SeasonDTO ss = new SeasonDTO("6rdCode", SeasonStatus.COMPLETED);

        mockMvc.perform(
                post("/resource-management/seasons")
                        .content(asJsonString(ss))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSeasonGETByCode() throws Exception {
        System.out.println(mockMvc.perform(
                get("/resource-management/seasons/{code}", "6rdCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

        )
                .andDo(print())
                .andExpect(status().isOk()));
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
    public void testSeasonput() throws Exception {
        SeasonDTO ss = new SeasonDTO("6rdCode", SeasonStatus.CURRENT);

        mockMvc.perform(
                put("/resource-management/seasons/{code}", "6rdCode")
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
                delete("/resource-management/seasons/{code}", "6rdCode")
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