package dbbwproject.serviceunit.pdfhandler;

import dbbwproject.serviceunit.dto.Gender;
import dbbwproject.serviceunit.dto.TypeOfService;
import dbbwproject.serviceunit.dto.TypeOfTravelDoc;
import dbbwproject.serviceunit.pdfhandler.passport.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class PasspostFormHandlerTest {

    @Test
    void savePassportForm() throws IOException {
        PassportForm form = new PassportForm();
        form.setTypeOfService(TypeOfService.ONE_DAY);
        form.setTypeOfTravelDoc(TypeOfTravelDoc.IDENTITY_CERTIFICATE);
        form.setOldPPNumber("123456789v");
        form.setNmrpNo("123456789v");
        form.setNicNo("0123456789vf");
        form.setSurName("SU defghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQR");
        form.setOtherNames("on defghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQR");
        form.setPermanentAddress("PA defghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQR");
        form.setDistrict("DI ABCDEFGHI");
        DateOfBirth dateOfBirth = new DateOfBirth();
        dateOfBirth.setDate("23");
        dateOfBirth.setMonth("08");
        dateOfBirth.setYear("1993");
        form.setDateOfBirth(dateOfBirth);
        form.setBirthCertificateNo("1245");
        form.setBirthCertificateDistrict("MY__DISTRICT");

        form.setPlaceOfBirth("PO_CDEFGHIJKLMNOPQRSTU");
        form.setGender(Gender.MALE);
        form.setJob("JOB_MUJOBISENGINEERING");
        form.setDualCitizen(DualCitizenship.YES);
        form.setDualCitizenNo("0123456789VK");
        form.setTpNoForPP("0771650539");
        form.setEmailAddress("nimashdilanka.13@gmail");
        form.setForeignNationality("FOR_NATIONALITY");
        form.setForeignPPNo("FOR_PASSPORT_NO");
//        form.setFatherNicNo("932361256vkr");
//        form.setMotherNicNo("123461256vko");
        PasspostFormHandler.saveAppForm(form, "newPassport.pdf");
    }

    @Test
    void savePassportFormTwo() throws IOException {
        PassportForm form = new PassportForm();
        form.setTypeOfService(TypeOfService.ONE_DAY);
        form.setTypeOfTravelDoc(TypeOfTravelDoc.ALL_COUNTRIES);
        form.setOldPPNumber("old trvl");
        form.setNmrpNo("nmrp no");
        form.setNicNo("932361256v");
        form.setSurName("DILANKA");
        form.setOtherNames("NIMASH SURNAME");
        form.setPermanentAddress("D7,FACTORY SIDE,HINGURANA,AMPARA");
        form.setDistrict("DIGAMADULLA");
        DateOfBirth dateOfBirth = new DateOfBirth();
        dateOfBirth.setDate("23");
        dateOfBirth.setMonth("08");
        dateOfBirth.setYear("1993");
        form.setDateOfBirth(dateOfBirth);
        form.setBirthCertificateDistrict("GALLE");
        form.setBirthCertificateNo("8069");
        form.setPlaceOfBirth("MAHAMODARA");
        form.setGender(Gender.MALE);
        form.setJob("SOFTWARE ENGINEER");
        form.setDualCitizen(DualCitizenship.YES);
        form.setDualCitizenNo("dual citi");
        form.setTpNoForPP("0771650539");
        form.setEmailAddress("nimashdilan@gmail.com");
        form.setForeignNationality("foregn nation");
        form.setForeignPPNo("for nati no");
//        form.setFatherNicNo("");
//        form.setMotherNicNo("");
        PasspostFormHandler.saveAppForm(form, "newPassportTwo.pdf");
    }
}