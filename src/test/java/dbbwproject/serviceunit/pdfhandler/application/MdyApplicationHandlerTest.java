package dbbwproject.serviceunit.pdfhandler.application;

import dbbwproject.serviceunit.dto.Gender;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class MdyApplicationHandlerTest {

    @Test
    void savePassportForm() throws IOException {
        MdyApplication md = new MdyApplication(
                "seasonCode",
                "10024",
                "2018-02-15",
                "45",
                "Dilanka",
                "Dammalage NImash",
                "main address lien",
                "my emails",
                "tp for pp",
                "tp for pp1",
                "tp for pp2",
                "1993-08-23",
                "Mahamodara",
                "932316256v",
                Gender.MALE,
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                "2018",
                true,
                false,
                true,
                false,
                true,
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                "Mihiri",
                "spouse birth place",
                "father name",
                "father birth",
                "mother name",
                "mother birth",
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                "other illness",
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                "owner name",
                "owner address",
                "owner tp no1",
                "owner tp no2",
                true,
                false
        );

        MdyApplicationHandler.saveAppForm(md, "newApplication.pdf");
    }
}