package dbbwproject.serviceunit.pdfhandler;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class PasspostFormHandlerTest {

    @Test
    void savePassportForm() throws IOException {
        PassportForm form = new PassportForm();
        form.setTypeOfService(TypeOfService.NORMAL);
        form.setTypeOfTravelDoc(TypeOfTravelDoc.ALL_COUNTRIES);
        form.setPresentTravelDocNo("0");
        form.setNmrpNo("1");
        form.setNicNo("2");
        form.setSurName("3");
        form.setOtherNames("3");
        form.setPermenentAddress("3");
        form.setDistrict("3");
        DateOfBirth dateOfBirth = new DateOfBirth();
        dateOfBirth.setDate("1");
        dateOfBirth.setMonth("1");
        dateOfBirth.setYear("1");
        form.setDateOfBirth(dateOfBirth);
        BirthCertificate bs = new BirthCertificate();
        bs.setNo("1");
        bs.setDistrict("1");
        form.setBirthCertificate(bs);
        form.setPlaceOfBirth("1");
        form.setSex(Sex.MALE);
        form.setJob("0");
        form.setDualCitizenship(DualCitizenship.YES);
        form.setDualCitizenshipNo("8");
        form.setMobileNo("7");
        form.setEmail("4");
        form.setForeignNationality("1");
        form.setForeignPassportNo("3");
        form.setFatherNicNo("9");
        form.setMotherNicNo("1");

        PasspostFormHandler.savePassportForm(form, "newPassport.pdf");

    }
}