package dbbwproject.serviceunit.pdfhandler.application;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import dbbwproject.serviceunit.pdfhandler.FormUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;

public class MdyApplicationHandler {
    private static int DEFAULT_FONT_SIZE = 9;

    public static ByteArrayInputStream generatePdfAsByteArray(MdyApplication mdApp) throws IOException {
        //passportForm.validate();
        File file = ResourceUtils.getFile("classpath:mahamegha_app_formv2.pdf");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (FileInputStream in = new FileInputStream(file);
                 PdfReader reader = new PdfReader(in);
                 PdfWriter writer = new PdfWriter(baos);
                 PdfDocument pdf = new PdfDocument(reader, writer)) {

                PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);

                FormUtils.setString("seasonCode", mdApp.getSeasonCode(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("tripCode", mdApp.getTripCode(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("tripStartDate", mdApp.getTripStartDate(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("registrationNumber", mdApp.getRegistrationNumber(), form, DEFAULT_FONT_SIZE);

                FormUtils.setString("surName", mdApp.getSurName(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("otherNames", mdApp.getOtherNames(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("permanentAddress", mdApp.getPermanentAddress(), form, DEFAULT_FONT_SIZE);
                //FormUtils.setString("permanentAddress_1", mdApp.getPermanentAddress_1(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("emailAddress", mdApp.getEmailAddress(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("tpNoForPP", mdApp.getTpNoForPP(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("tpNoForContacts1", mdApp.getTpNoForContacts1(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("tpNoForContacts2", mdApp.getTpNoForContacts2(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("dateOfBirth", mdApp.getDateOfBirth(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("placeOfBirth", mdApp.getPlaceOfBirth(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("nicNo", mdApp.getNicNo(), form, DEFAULT_FONT_SIZE);
                FormUtils.setBooleanDefualt(mdApp.getGender().toString(), form);

                FormUtils.setBooleanChkOrCross("bcReceived", mdApp.isBcReceived(), form);
                FormUtils.setBooleanChkOrCross("bcCopyReceived", mdApp.isBcCopyReceived(), form);
                FormUtils.setBooleanChkOrCross("nicReceived", mdApp.isNicReceived(), form);
                FormUtils.setBooleanChkOrCross("oldNicReceived", mdApp.isOldNicReceived(), form);
                FormUtils.setBooleanChkOrCross("certifiedPhotoReceived", mdApp.isCertifiedPhotoReceived(), form);
                FormUtils.setBooleanChkOrCross("copyOfNicReceived", mdApp.isCopyOfNicReceived(), form);
                FormUtils.setBooleanChkOrCross("voCertfiedNameReceived", mdApp.isVoCertfiedNameReceived(), form);
                FormUtils.setBooleanChkOrCross("voCertfiedDobReceived", mdApp.isVoCertfiedDobReceived(), form);

                FormUtils.setBooleanChkOrCross("wentAbroadBefore", mdApp.isWentAbroadBefore(), form);
                FormUtils.setString("lastMigrateYear", mdApp.getLastMigrateYear(), form, DEFAULT_FONT_SIZE);
                FormUtils.setBooleanChkOrCross("oldPassportAvailability", mdApp.isOldPassportAvailability(), form);
                FormUtils.setBooleanChkOrCross("policeRepAvailForOldPP", mdApp.isPoliceRepAvailForOldPP(), form);
                FormUtils.setBooleanChkOrCross("informPenaltyForLostPP", mdApp.isInformPenaltyForLostPP(), form);
                FormUtils.setBooleanChkOrCross("confirmPPAvailForTrip", mdApp.isConfirmPPAvailForTrip(), form);
                FormUtils.setBooleanChkOrCross("includeJobInPP", mdApp.isIncludeJobInPP(), form);

                FormUtils.setBooleanChkOrCross("parentApprovalLetterReceived", mdApp.isParentApprovalLetterReceived(), form);
                FormUtils.setBooleanChkOrCross("motherNicCopyReceived", mdApp.isMotherNicCopyReceived(), form);
                FormUtils.setBooleanChkOrCross("fatherNicCopyReceived", mdApp.isFatherNicCopyReceived(), form);
                FormUtils.setBooleanChkOrCross("motherPpCopyReceived", mdApp.isMotherPpCopyReceived(), form);
                FormUtils.setBooleanChkOrCross("fatherPpCopyReceived", mdApp.isFatherPpCopyReceived(), form);
                FormUtils.setBooleanChkOrCross("parentMarriageCertificateReceived", mdApp.isParentMarriageCertificateReceived(), form);
                FormUtils.setBooleanChkOrCross("informedParentAccompany", mdApp.isInformedParentAccompany(), form);
                FormUtils.setBooleanChkOrCross("parentCertifiedLetter", mdApp.isParentCertifiedLetter(), form);

                FormUtils.setString("spouseName", mdApp.getSpouseName(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("spouseBirthPlace", mdApp.getSpouseBirthPlace(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("fatherName", mdApp.getFatherName(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("fatherBirthPlace", mdApp.getFatherBirthPlace(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("motherName", mdApp.getMotherName(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("motherBirthPlace", mdApp.getMotherBirthPlace(), form, DEFAULT_FONT_SIZE);

                FormUtils.setBooleanChkOrCross("diabetic", mdApp.isDiabetic(), form);
                FormUtils.setBooleanChkOrCross("highBloodPressure", mdApp.isHighBloodPressure(), form);
                FormUtils.setBooleanChkOrCross("arthritis", mdApp.isArthritis(), form);
                FormUtils.setBooleanChkOrCross("wheezing", mdApp.isWheezing(), form);
                FormUtils.setBooleanChkOrCross("heartIllness", mdApp.isHeartIllness(), form);
                FormUtils.setBooleanChkOrCross("epilepsy", mdApp.isEpilepsy(), form);
                FormUtils.setBooleanChkOrCross("medicalReport", mdApp.isMedicalReport(), form);
                FormUtils.setBooleanChkOrCross("otherIllnessExist", mdApp.isOtherIllnessExist(), form);
                FormUtils.setString("otherIllnessDetail", mdApp.getOtherIllnessDetail(), form, DEFAULT_FONT_SIZE);
                FormUtils.setBooleanChkOrCross("surgery", mdApp.isSurgery(), form);
                FormUtils.setBooleanChkOrCross("artificialHandLeg", mdApp.isArtificialHandLeg(), form);
                FormUtils.setBooleanChkOrCross("mentalIllness", mdApp.isMentalIllness(), form);
                FormUtils.setBooleanChkOrCross("militaryPerson", mdApp.isMilitaryPerson(), form);
                FormUtils.setBooleanChkOrCross("resignedFrmMilitary", mdApp.isResignedFrmMilitary(), form);
                FormUtils.setBooleanChkOrCross("forbiddenLeaveCountry", mdApp.isForbiddenLeaveCountry(), form);
                FormUtils.setBooleanChkOrCross("informCourtMilitaryIssues", mdApp.isInformCourtMilitaryIssues(), form);

                FormUtils.setString("ownerName", mdApp.getOwnerName(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("ownerAddress", mdApp.getOwnerAddress(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("ownerTPNo1", mdApp.getOwnerTPNo1(), form, DEFAULT_FONT_SIZE);
                FormUtils.setString("ownerTPNo2", mdApp.getOwnerTPNo2(), form, DEFAULT_FONT_SIZE);

                FormUtils.setBooleanChkOrCross("ownerCertifiedLetterOldPax", mdApp.isOwnerCertifiedLetterOldPax(), form);
                FormUtils.setBooleanChkOrCross("judgeCertifiedLetterOldPax", mdApp.isJudgeCertifiedLetterOldPax(), form);

                form.flattenFields();
            }
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    public static void saveAppForm(MdyApplication mdApp, String fileName) throws IOException {
        ByteArrayInputStream in = generatePdfAsByteArray(mdApp);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);

        File file = new File(fileName);
        Files.write(file.toPath(), buffer);

    }

}