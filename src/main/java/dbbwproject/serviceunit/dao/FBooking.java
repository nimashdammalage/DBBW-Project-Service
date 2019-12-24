package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.Gender;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FBooking {
    public static String key = "bookings";
    public static final String SEASON_TRIP_INDEX = "seasonTripIndex";
    public static final String SEASON_TRIP_PNAME_INDEX = "seasonTripPNameIndex";
    //region Meta Data of booking
    private String seasonCode;
    private String tripCode;
    private String pbPersonName;
    private String tripStartDate;
    private int registrationNumber;
    private String seasonTripIndex;
    private String seasonTripPNameIndex;
    //endregion

    //region Personal Details
    private String surname;
    private String otherNames;
    private String nicAddress;
    private String permanentAddress;
    private String emailAddress;
    private String tpNoForPP;
    private String tpNoForContacts1;
    private String tpNoForContacts2;
    private String dateOfBirth;
    private String placeOfBirth;
    private String nicNo;
    private Gender sex;
    private String job;
    private boolean married;
    private String spouseName;
    private String spouseBirthPlace;
    private String fatherName;
    private String fatherBirthPlace;
    private String motherName;
    private String motherBirthPlace;
    private String ownerName;
    private String ownerAddress;
    private String ownerTPNo1;
    private String ownerTPNo2;
    //endregion

    //region Health and Military Requirements
    private boolean diabetic;
    private boolean highBloodPressure;
    private boolean arthritis;
    private boolean wheezing;
    private boolean heartIllness;
    private boolean epilepsy;
    private boolean medicalReport;
    private boolean otherIllnessExist;
    private String otherIllnessDetail;
    private boolean surgery;
    private boolean artificialHandLeg;
    private boolean mentalIllness;
    private boolean militaryPerson;
    private boolean resignedFrmMilitary;
    private boolean forbiddenLeaveCountry;
    private boolean informCourtMilitaryIssues;
    //endregion

    //region Personal Documents
    private boolean BCReceived;
    private boolean BCCopyReceived;
    private boolean NICReceived;
    private boolean OldNICReceived;
    private boolean certifiedPhotoReceived;
    private boolean copyOfNICReceived;
    private boolean voCertfiedNameReceived;
    private boolean voCertfiedDOBReceived;
    //endregion

    //region Passport and Dual Nationality
    private boolean needToCreatePP;
    private String nmrpNo;
    private String typeOfService;
    private String typeOfTravelDoc;
    private boolean wentAbroadBefore;
    private String lastMigrateYear;
    private boolean oldPassportAvailability;
    private String oldPPNumber;
    private String oldPPExpDate;
    private boolean policeRepAvailForOldPP;
    private boolean informPenaltyForLostPP;
    private boolean confirmPPAvailForTrip;
    private boolean includeJobInPP;
    private boolean jobAppointmntLtrRcvd;
    private boolean dualCitizen;
    private String dualCitizenNo;
    private String foreignNationality;
    private String foreignPPNo;
    private boolean faxFormDualCitizen;
    //endregion

    //region Special Requirements
    private boolean parentApprovalLetterReceived;
    private boolean motherNICCopyReceived;
    private boolean fatherNICCopyReceived;
    private boolean motherPPCopyReceived;
    private boolean fatherPPCopyReceived;
    private boolean parentMarriageCertificateReceived;
    private boolean informedParentAccompany;
    private boolean parentCertifiedLetter;
    private boolean ownerCertifiedLetterOldPax;
    private boolean judgeCertifiedLetterOldPax;
    //endregion

}

