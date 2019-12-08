package dbbwproject.serviceunit.pdfhandler;

import dbbwproject.serviceunit.pdfhandler.passport.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class PasspostFormHandlerTest {

    @Test
    void savePassportForm() throws IOException {
        PassportForm form = new PassportForm();
        form.setTypeOfService(TypeOfService.ONE_DAY);
        form.setTypeOfTravelDoc(TypeOfTravelDoc.IDENTITY_CERTIFICATE);
        form.setPresentTravelDocNo("123456789v");
        form.setNmrpNo("123456789v");
        form.setNicNo("0123456789vf");
        form.setSurName("SU defghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQR");
        form.setOtherNames("on defghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQR");
        form.setPermenentAddress("PA defghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQR");
        form.setDistrict("DI ABCDEFGHI");
        DateOfBirth dateOfBirth = new DateOfBirth();
        dateOfBirth.setDate("23");
        dateOfBirth.setMonth("08");
        dateOfBirth.setYear("1993");
        form.setDateOfBirth(dateOfBirth);
        BirthCertificate bs = new BirthCertificate();
        bs.setNo("1234");
        bs.setDistrict("MY__DISTRICT");
        form.setBirthCertificate(bs);
        form.setPlaceOfBirth("PO_CDEFGHIJKLMNOPQRSTU");
        form.setSex(Sex.MALE);
        form.setJob("JOB_MUJOBISENGINEERING");
        form.setDualCitizenship(DualCitizenship.YES);
        form.setDualCitizenshipNo("0123456789VK");
        form.setMobileNo("0771650539");
        form.setEmail("nimashdilanka.13@gmail");
        form.setForeignNationality("FOR_NATIONALITY");
        form.setForeignPassportNo("FOR_PASSPORT_NO");
        form.setFatherNicNo("932361256vkr");
        form.setMotherNicNo("123461256vko");
        PasspostFormHandler.savePassportForm(form, "newPassport.pdf");
    }

    @Test
    void savePassportFormTwo() throws IOException {
        PassportForm form = new PassportForm();
        form.setTypeOfService(TypeOfService.ONE_DAY);
        form.setTypeOfTravelDoc(TypeOfTravelDoc.ALL_COUNTRIES);
        form.setPresentTravelDocNo("");
        form.setNmrpNo("");
        form.setNicNo("932361256v");
        form.setSurName("DILANKA");
        form.setOtherNames("");
        form.setPermenentAddress("D7,FACTORY SIDE,HINGURANA,AMPARA");
        form.setDistrict("DIGAMADULLA");
        DateOfBirth dateOfBirth = new DateOfBirth();
        dateOfBirth.setDate("23");
        dateOfBirth.setMonth("08");
        dateOfBirth.setYear("1993");
        form.setDateOfBirth(dateOfBirth);
        BirthCertificate bs = new BirthCertificate();
        bs.setNo("8069");
        bs.setDistrict("GALLE");
        form.setBirthCertificate(bs);
        form.setPlaceOfBirth("MAHAMODARA");
        form.setSex(Sex.MALE);
        form.setJob("SOFTWARE ENGINEER");
        form.setDualCitizenship(DualCitizenship.NO);
        form.setDualCitizenshipNo("");
        form.setMobileNo("0771650539");
        form.setEmail("nimashdilan@gmail.com");
        form.setForeignNationality("");
        form.setForeignPassportNo("");
        form.setFatherNicNo("");
        form.setMotherNicNo("");
        PasspostFormHandler.savePassportForm(form, "newPassportTwo.pdf");
    }
}