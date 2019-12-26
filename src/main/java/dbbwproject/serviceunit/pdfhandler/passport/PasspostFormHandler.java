package dbbwproject.serviceunit.pdfhandler.passport;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;

import static dbbwproject.serviceunit.pdfhandler.FormUtils.setCharList;
import static dbbwproject.serviceunit.pdfhandler.FormUtils.setString;

public class PasspostFormHandler {
    private static int DEFAULT_FONT_SIZE = 9;

    public static ByteArrayInputStream generatePdfAsByteArray(PassportForm passportForm) throws IOException {
        File file = ResourceUtils.getFile("classpath:passportApplicationTemplate.pdf");
        passportForm.validate();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (FileInputStream in = new FileInputStream(file);
                 PdfReader reader = new PdfReader(in);
                 PdfWriter writer = new PdfWriter(baos);
                 PdfDocument pdf = new PdfDocument(reader, writer)) {

                PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);

                form.getField(passportForm.getTypeOfService().getFormFieldName()).setValue("Yes").setBorderWidth(0);
                form.getField(passportForm.getTypeOfTravelDoc().getFormFieldName()).setValue("Yes").setBorderWidth(0);
                setCharList("presentTravelDocNo", passportForm.getOldPPNumber(), form, DEFAULT_FONT_SIZE);
                setCharList("nmrpNo", passportForm.getNmrpNo(), form, DEFAULT_FONT_SIZE);
                setCharList("nicNo", passportForm.getNicNo(), form, DEFAULT_FONT_SIZE);
                setCharList("surName", passportForm.getSurName(), form, DEFAULT_FONT_SIZE);
                setCharList("otherNames", passportForm.getOtherNames(), form, DEFAULT_FONT_SIZE);
                setCharList("permenentAddress", passportForm.getPermanentAddress(), form, DEFAULT_FONT_SIZE);
                setCharList("district", passportForm.getDistrict(), form, DEFAULT_FONT_SIZE);
                setCharList("dob.date", passportForm.getDateOfBirth().getDate(), form, DEFAULT_FONT_SIZE);
                setCharList("dob.month", passportForm.getDateOfBirth().getMonth(), form, DEFAULT_FONT_SIZE);
                setCharList("dob.year", passportForm.getDateOfBirth().getYear(), form, DEFAULT_FONT_SIZE);
                setCharList("birthCertificate.no", passportForm.getBirthCertificateNo(), form, DEFAULT_FONT_SIZE);
                setCharList("birthCertificate.district", passportForm.getBirthCertificateDistrict(), form, DEFAULT_FONT_SIZE);
                setCharList("placeOfBirth", passportForm.getPlaceOfBirth(), form, DEFAULT_FONT_SIZE);
                form.getField(passportForm.getGender().getFormFieldName()).setValue("Yes").setBorderWidth(0);
                setCharList("job", passportForm.getJob(), form, DEFAULT_FONT_SIZE);
                form.getField(passportForm.getDualCitizen().getFormFieldName()).setValue("Yes").setBorderWidth(0);
                setCharList("dualCitizenshipNo", passportForm.getDualCitizenNo(), form, DEFAULT_FONT_SIZE);
                setCharList("mobileNo", passportForm.getTpNoForPP(), form, DEFAULT_FONT_SIZE);
                setCharList("email", passportForm.getEmailAddress(), form, DEFAULT_FONT_SIZE);
                setString("foreignNationality", passportForm.getForeignNationality(), form, DEFAULT_FONT_SIZE);
                setString("foreignPassportNo", passportForm.getForeignPPNo(), form, DEFAULT_FONT_SIZE);
                //setCharList("fatherNicNo", passportForm.getFatherNicNo(), form, DEFAULT_FONT_SIZE);
                //setCharList("motherNicNo", passportForm.getMotherNicNo(), form, DEFAULT_FONT_SIZE);
                form.flattenFields();

            }
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    public static void saveAppForm(PassportForm passportForm, String fileName) throws IOException {
        ByteArrayInputStream in = generatePdfAsByteArray(passportForm);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);

        File file = new File(fileName);
        Files.write(file.toPath(), buffer);
    }
}
