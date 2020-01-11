package dbbwproject.serviceunit.dao;

import dbbwproject.serviceunit.dto.BookingStatus;
import dbbwproject.serviceunit.dto.Gender;
import dbbwproject.serviceunit.dto.TypeOfService;
import dbbwproject.serviceunit.dto.TypeOfTravelDoc;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "registrationNumber")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reg_id", referencedColumnName = "id", nullable = false)
    private RegNumber registrationNumber;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    @Column(length = 44)
    private String surName;
    @Column(length = 44)
    private String otherNames;
    @Column(length = 44)
    private String nicAddress;
    @Column(length = 44)
    private String permanentAddress;
    @Column(length = 12)
    private String district;
    @Column(length = 22)
    private String emailAddress;
    @Column(length = 10)
    private String tpNoForPp;
    @Column(length = 10)
    private String tpNoForContacts1;
    @Column(length = 10)
    private String tpNoForContacts2;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Column(length = 4)
    private String birthCertificateNo;
    @Column(length = 12)
    private String birthCertificateDistrict;
    @Column(length = 22)
    private String placeOfBirth;
    @Column(length = 12)
    private String nicNo;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(length = 22)
    private String job;
    private Boolean married;
    @Column(length = 22)
    private String spouseName;
    @Column(length = 22)
    private String spouseBirthPlace;
    @Column(length = 22)
    private String fatherName;
    @Column(length = 22)
    private String fatherBirthPlace;
    @Column(length = 22)
    private String motherName;
    @Column(length = 22)
    private String motherBirthPlace;
    @Column(length = 22)
    private String ownerName;
    @Column(length = 44)
    private String ownerAddress;
    @Column(length = 10)
    private String ownerTpNo1;
    @Column(length = 10)
    private String ownerTpNo2;
    private Boolean diabetic;
    private Boolean highBloodPressure;
    private Boolean arthritis;
    private Boolean wheezing;
    private Boolean heartIllness;
    private Boolean epilepsy;
    private Boolean medicalReport;
    private Boolean otherIllnessExist;
    @Column(length = 22)
    private String otherIllnessDetail;
    private Boolean surgery;
    private Boolean artificialHandLeg;
    private Boolean mentalIllness;
    private Boolean militaryPerson;
    private Boolean resignedFrmMilitary;
    private Boolean forbiddenLeaveCountry;
    private Boolean informCourtMilitaryIssues;
    private Boolean bcReceived;
    private Boolean bcCopyReceived;
    private Boolean nicReceived;
    private Boolean oldNicReceived;
    private Boolean certifiedPhotoReceived;
    private Boolean copyOfNicReceived;
    private Boolean voCertfiedNameReceived;
    private Boolean voCertfiedDobReceived;
    private Boolean needToCreatePp;
    @Column(length = 10)
    private String nmrpNo;
    @Enumerated(EnumType.STRING)
    private TypeOfService typeOfService;
    @Enumerated(EnumType.STRING)
    private TypeOfTravelDoc typeOfTravelDoc;
    private Boolean wentAbroadBefore;
    @Column(length = 4)
    private String lastMigrateYear;
    private Boolean oldPassportAvailability;
    @Column(length = 10)
    private String oldPpNumber;
    @Temporal(TemporalType.DATE)
    private Date oldPpExpDate;
    private Boolean policeRepAvailForOldPp;
    private Boolean informPenaltyForLostPp;
    private Boolean confirmPpAvailForTrip;
    private Boolean includeJobInPp;
    private Boolean jobAppointmntLtrRcvd;
    private Boolean dualCitizen;
    @Column(length = 12)
    private String dualCitizenNo;
    @Column(length = 50)
    private String foreignNationality;
    @Column(length = 50)
    private String foreignPpNo;
    private Boolean faxFormDualCitizen;
    private Boolean parentApprovalLetterReceived;
    private Boolean motherNicCopyReceived;
    private Boolean fatherNicCopyReceived;
    private Boolean motherPpCopyReceived;
    private Boolean fatherPpCopyReceived;
    private Boolean parentMarriageCertificateReceived;
    private Boolean informedParentAccompany;
    private Boolean parentCertifiedLetter;
    private Boolean ownerCertifiedLetterOldPax;
    private Boolean judgeCertifiedLetterOldPax;
    private Integer createdBy;
    private Integer modifiedBy;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTimestamp;
}
