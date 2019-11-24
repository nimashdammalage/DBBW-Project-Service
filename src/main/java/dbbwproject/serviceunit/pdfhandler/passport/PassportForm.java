package dbbwproject.serviceunit.pdfhandler.passport;

import dbbwproject.serviceunit.pdfhandler.Validate;
import lombok.Data;

@Data
public class PassportForm implements Validate {
    private TypeOfService typeOfService;
    private TypeOfTravelDoc typeOfTravelDoc;
    private String presentTravelDocNo;
    private String nmrpNo;
    private String nicNo;
    private String surName;
    private String otherNames;
    private String permenentAddress;
    private String district;
    private DateOfBirth dateOfBirth;
    private BirthCertificate birthCertificate;
    private String placeOfBirth;
    private Sex sex;
    private String job;
    private DualCitizenship dualCitizenship;
    private String dualCitizenshipNo;
    private String mobileNo;
    private String email;
    private String foreignNationality;
    private String foreignPassportNo;
    private String fatherNicNo;
    private String motherNicNo;

    @Override
    public void validate() {
        validateEnum(typeOfService, "typeOfService");
        validateEnum(typeOfTravelDoc, "typeOfTravelDoc");
        validateField(presentTravelDocNo, PassportFormConstants.PRESENT_TRAVEL_DOC_NO_LENGTH, "presentTravelDocNo");
        validateField(nmrpNo, PassportFormConstants.NMRP_NO_LENGTH, "nmrpNo");
        validateField(nicNo, PassportFormConstants.NIC_NO_LENGTH, "nicNo");
        validateField(surName, PassportFormConstants.SURNAME_LENGTH, "surName");
        validateField(otherNames, PassportFormConstants.OTHERNAMES_LENGTH, "otherNames");
        validateField(permenentAddress, PassportFormConstants.PERMENET_ADDRESS_LENGTH, "permenentAddress");
        validateField(district, PassportFormConstants.DISTRICT_LENGTH, "district");
        dateOfBirth.validate();
        birthCertificate.validate();
        validateField(placeOfBirth, PassportFormConstants.PLACE_OF_BIRTH_LENGTH, "placeOfBirth");
        validateEnum(sex, "sex");
        validateField(job, PassportFormConstants.JOB_LENGTH, "job");
        validateEnum(dualCitizenship, "dualCitizenship");
        validateField(dualCitizenshipNo, PassportFormConstants.DUAL_CITIZENSHIP_NO_LENGTH, "mobileNo");
        validateField(mobileNo, PassportFormConstants.MOBILE_NO_LENGTH, "mobileNo");
        validateField(email, PassportFormConstants.EMAIL_LENGTH, "email");
        validateField(fatherNicNo, PassportFormConstants.FATHER_NIC_NO_LENGTH, "fatherNicNo");
        validateField(motherNicNo, PassportFormConstants.MOTHER_NIC_NO_LENGTH, "motherNicNo");
    }

    private static void validateField(String field, int length, String fieldName) {
        if (field == null) {
            throw new IllegalStateException(fieldName + "can not be null in PassportForm object.");
        }
        if (field.length() > length) {
            throw new IllegalStateException(fieldName + "size must be less than or equal to " + length + " .");
        }
    }

    private static void validateEnum(Enum e, String fieldName) {
        if (e == null) {
            throw new IllegalStateException(fieldName + "can not be null in PassportForm object.");
        }
    }
}
