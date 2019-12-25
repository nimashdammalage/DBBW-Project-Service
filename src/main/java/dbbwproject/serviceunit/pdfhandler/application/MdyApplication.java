package dbbwproject.serviceunit.pdfhandler.application;

import dbbwproject.serviceunit.dto.Gender;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MdyApplication {
    // Meta Data of booking
    private String seasonCode;
    private String tripCode;
    private String tripStartDate;
    private String registrationNumber;

    // Personal Details -1
    private String surName;
    private String otherNames;
    private String permanentAddress;
    //private String permanentAddress_1;
    private String emailAddress;
    private String tpNoForPP;
    private String tpNoForContacts1;
    private String tpNoForContacts2;
    private String dateOfBirth;
    private String placeOfBirth;
    private String nicNo;
    private Gender gender;

    // Personal Documents
    private boolean bcReceived;
    private boolean bcCopyReceived;
    private boolean nicReceived;
    private boolean oldNicReceived;
    private boolean certifiedPhotoReceived;
    private boolean copyOfNicReceived;
    private boolean voCertfiedNameReceived;
    private boolean voCertfiedDobReceived;

    //Pasport and dual nationality
    private boolean wentAbroadBefore;
    private String lastMigrateYear;
    private boolean oldPassportAvailability;
    private boolean policeRepAvailForOldPP;
    private boolean informPenaltyForLostPP;
    private boolean confirmPPAvailForTrip;
    private boolean includeJobInPP;

    //Special requirements young child
    private boolean parentApprovalLetterReceived;
    private boolean motherNicCopyReceived;
    private boolean fatherNicCopyReceived;
    private boolean motherPpCopyReceived;
    private boolean fatherPpCopyReceived;
    private boolean parentMarriageCertificateReceived;
    private boolean informedParentAccompany;
    private boolean parentCertifiedLetter;

    // Personal Details -2
    private String spouseName;
    private String spouseBirthPlace;
    private String fatherName;
    private String fatherBirthPlace;
    private String motherName;
    private String motherBirthPlace;

    // Health and Military Requirements
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

    // Personal Details -2
    private String ownerName;
    private String ownerAddress;
    private String ownerTPNo1;
    private String ownerTPNo2;

    //Special requirements young old person
    private boolean ownerCertifiedLetterOldPax;
    private boolean judgeCertifiedLetterOldPax;


}
