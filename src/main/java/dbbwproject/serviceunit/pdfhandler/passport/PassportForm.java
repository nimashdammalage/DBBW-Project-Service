package dbbwproject.serviceunit.pdfhandler.passport;

import dbbwproject.serviceunit.dto.Gender;
import dbbwproject.serviceunit.dto.TypeOfService;
import dbbwproject.serviceunit.dto.TypeOfTravelDoc;
import dbbwproject.serviceunit.pdfhandler.Validate;
import lombok.Data;
import org.springframework.web.client.ResourceAccessException;

@Data
public class PassportForm implements Validate {
    private TypeOfService typeOfService;
    private TypeOfTravelDoc typeOfTravelDoc;
    private String oldPPNumber;
    private String nmrpNo;
    private String nicNo;
    private String surName;
    private String otherNames;
    private String permanentAddress;
    private String district;
    private DateOfBirth dateOfBirth;
    private String birthCertificateNo;
    private String birthCertificateDistrict;
    private String placeOfBirth;
    private Gender gender;
    private String job;
    private DualCitizenship dualCitizen;
    private String dualCitizenNo;
    private String tpNoForPP;
    private String emailAddress;
    private String foreignNationality;
    private String foreignPPNo;
    //private String fatherNicNo;
    //private String motherNicNo;

    @Override
    public void validate() {
        validateEnum(typeOfService, "typeOfService");
        validateEnum(typeOfTravelDoc, "typeOfTravelDoc");
        validateField(oldPPNumber, PassportFormConstants.PRESENT_TRAVEL_DOC_NO_LENGTH, "presentTravelDocNo");
        validateField(nmrpNo, PassportFormConstants.NMRP_NO_LENGTH, "nmrpNo");
        validateField(nicNo, PassportFormConstants.NIC_NO_LENGTH, "nicNo");
        validateField(surName, PassportFormConstants.SURNAME_LENGTH, "surName");
        validateField(otherNames, PassportFormConstants.OTHERNAMES_LENGTH, "otherNames");
        validateField(permanentAddress, PassportFormConstants.PERMENET_ADDRESS_LENGTH, "permenentAddress");
        validateField(district, PassportFormConstants.DISTRICT_LENGTH, "district");
        dateOfBirth.validate();
        validateField(birthCertificateNo, PassportFormConstants.BC_NO_LENGTH, "birthCertificateNo");
        validateField(birthCertificateDistrict, PassportFormConstants.DISTRICT_LENGTH, "birthCertificateDistrict");
        validateField(placeOfBirth, PassportFormConstants.PLACE_OF_BIRTH_LENGTH, "placeOfBirth");
        validateEnum(gender, "gender");
        validateField(job, PassportFormConstants.JOB_LENGTH, "job");
        validateEnum(dualCitizen, "dualCitizenship");
        validateField(dualCitizenNo, PassportFormConstants.DUAL_CITIZENSHIP_NO_LENGTH, "mobileNo");
        validateField(tpNoForPP, PassportFormConstants.MOBILE_NO_LENGTH, "mobileNo");
        validateField(emailAddress, PassportFormConstants.EMAIL_LENGTH, "email");
        //validateField(fatherNicNo, PassportFormConstants.FATHER_NIC_NO_LENGTH, "fatherNicNo");
        //validateField(motherNicNo, PassportFormConstants.MOTHER_NIC_NO_LENGTH, "motherNicNo");
    }

    private static void validateField(String field, int length, String fieldName) {
        if (field == null) {
            throw new ResourceAccessException(fieldName + " can not be null in PassportForm object.");
        }
        if (field.length() > length) {
            throw new ResourceAccessException(fieldName + " size must be less than or equal to " + length + " .");
        }
    }

    private static void validateEnum(Enum e, String fieldName) {
        if (e == null) {
            throw new ResourceAccessException(fieldName + "can not be null in PassportForm object.");
        }
    }
}
