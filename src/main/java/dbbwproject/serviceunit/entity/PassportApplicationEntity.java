package dbbwproject.serviceunit.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "passport_application", schema = "dbbw", catalog = "")
public class PassportApplicationEntity {
    private int id;
    private String presentTrDocNo;
    private String nmrpNo;
    private String nicNo;
    private String surname;
    private String permenentAddress;
    private String otherNames;
    private String district;
    private Date dateOfBirth;
    private String birthCertificateNo;
    private String birthCertificateDistrict;
    private String placeOfBirth;
    private String job;
    private String dualCitizenshipNo;
    private String mobileNo;
    private String email;
    private String foreignNationality;
    private String foreignPassportNo;
    private String fatherNicNo;
    private String motherNicNo;
    private DualCitizenshipEntity dualCitizenshipByDualCitizenshipId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "present_tr_doc_no", nullable = true, length = 12)
    public String getPresentTrDocNo() {
        return presentTrDocNo;
    }

    public void setPresentTrDocNo(String presentTrDocNo) {
        this.presentTrDocNo = presentTrDocNo;
    }

    @Basic
    @Column(name = "nmrp_no", nullable = true, length = 10)
    public String getNmrpNo() {
        return nmrpNo;
    }

    public void setNmrpNo(String nmrpNo) {
        this.nmrpNo = nmrpNo;
    }

    @Basic
    @Column(name = "nic_no", nullable = false, length = 12)
    public String getNicNo() {
        return nicNo;
    }

    public void setNicNo(String nicNo) {
        this.nicNo = nicNo;
    }

    @Basic
    @Column(name = "surname", nullable = false, length = 44)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "permenent_address", nullable = true, length = 44)
    public String getPermenentAddress() {
        return permenentAddress;
    }

    public void setPermenentAddress(String permenentAddress) {
        this.permenentAddress = permenentAddress;
    }

    @Basic
    @Column(name = "other_names", nullable = true, length = 44)
    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    @Basic
    @Column(name = "district", nullable = true, length = 12)
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Basic
    @Column(name = "date_of_birth", nullable = false)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Basic
    @Column(name = "birth_certificate_no", nullable = false, length = 4)
    public String getBirthCertificateNo() {
        return birthCertificateNo;
    }

    public void setBirthCertificateNo(String birthCertificateNo) {
        this.birthCertificateNo = birthCertificateNo;
    }

    @Basic
    @Column(name = "birth_certificate_district", nullable = true, length = 12)
    public String getBirthCertificateDistrict() {
        return birthCertificateDistrict;
    }

    public void setBirthCertificateDistrict(String birthCertificateDistrict) {
        this.birthCertificateDistrict = birthCertificateDistrict;
    }

    @Basic
    @Column(name = "place_of_birth", nullable = true, length = 22)
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    @Basic
    @Column(name = "job", nullable = true, length = 22)
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Basic
    @Column(name = "dual_citizenship_no", nullable = true, length = 12)
    public String getDualCitizenshipNo() {
        return dualCitizenshipNo;
    }

    public void setDualCitizenshipNo(String dualCitizenshipNo) {
        this.dualCitizenshipNo = dualCitizenshipNo;
    }

    @Basic
    @Column(name = "mobile_no", nullable = true, length = 10)
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 22)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "foreign_nationality", nullable = true, length = 100)
    public String getForeignNationality() {
        return foreignNationality;
    }

    public void setForeignNationality(String foreignNationality) {
        this.foreignNationality = foreignNationality;
    }

    @Basic
    @Column(name = "foreign_passport_no", nullable = true, length = 100)
    public String getForeignPassportNo() {
        return foreignPassportNo;
    }

    public void setForeignPassportNo(String foreignPassportNo) {
        this.foreignPassportNo = foreignPassportNo;
    }

    @Basic
    @Column(name = "father_nic_no", nullable = true, length = 10)
    public String getFatherNicNo() {
        return fatherNicNo;
    }

    public void setFatherNicNo(String fatherNicNo) {
        this.fatherNicNo = fatherNicNo;
    }

    @Basic
    @Column(name = "mother_nic_no", nullable = true, length = 10)
    public String getMotherNicNo() {
        return motherNicNo;
    }

    public void setMotherNicNo(String motherNicNo) {
        this.motherNicNo = motherNicNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassportApplicationEntity that = (PassportApplicationEntity) o;
        return id == that.id &&
                Objects.equals(presentTrDocNo, that.presentTrDocNo) &&
                Objects.equals(nmrpNo, that.nmrpNo) &&
                Objects.equals(nicNo, that.nicNo) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(permenentAddress, that.permenentAddress) &&
                Objects.equals(otherNames, that.otherNames) &&
                Objects.equals(district, that.district) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(birthCertificateNo, that.birthCertificateNo) &&
                Objects.equals(birthCertificateDistrict, that.birthCertificateDistrict) &&
                Objects.equals(placeOfBirth, that.placeOfBirth) &&
                Objects.equals(job, that.job) &&
                Objects.equals(dualCitizenshipNo, that.dualCitizenshipNo) &&
                Objects.equals(mobileNo, that.mobileNo) &&
                Objects.equals(email, that.email) &&
                Objects.equals(foreignNationality, that.foreignNationality) &&
                Objects.equals(foreignPassportNo, that.foreignPassportNo) &&
                Objects.equals(fatherNicNo, that.fatherNicNo) &&
                Objects.equals(motherNicNo, that.motherNicNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, presentTrDocNo, nmrpNo, nicNo, surname, permenentAddress, otherNames, district, dateOfBirth, birthCertificateNo, birthCertificateDistrict, placeOfBirth, job, dualCitizenshipNo, mobileNo, email, foreignNationality, foreignPassportNo, fatherNicNo, motherNicNo);
    }

    @ManyToOne
    @JoinColumn(name = "dual_citizenship_id", referencedColumnName = "id", nullable = false)
    public DualCitizenshipEntity getDualCitizenshipByDualCitizenshipId() {
        return dualCitizenshipByDualCitizenshipId;
    }

    public void setDualCitizenshipByDualCitizenshipId(DualCitizenshipEntity dualCitizenshipByDualCitizenshipId) {
        this.dualCitizenshipByDualCitizenshipId = dualCitizenshipByDualCitizenshipId;
    }
}
