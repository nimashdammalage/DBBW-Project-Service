package dbbwproject.serviceunit.pdfhandler.passport;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;

import java.io.*;

import static dbbwproject.serviceunit.pdfhandler.FormUtils.setCharList;
import static dbbwproject.serviceunit.pdfhandler.FormUtils.setString;

public class PasspostFormHandler {
    private static int DEFAULT_FONT_SIZE = 9;

    public static void savePassportForm(PassportForm passportForm, String fileLocaion) throws IOException {
        passportForm.validate();
        File file = ResourceUtils.getFile("classpath:passportApplicationTemplate.pdf");
        try (PdfDocument pdf = new PdfDocument(new PdfReader(new FileInputStream(file)),
                new PdfWriter(new FileOutputStream(fileLocaion)))) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);

            form.getField(passportForm.getTypeOfService().getFormFieldName()).setValue("Yes").setBorderWidth(0);
            form.getField(passportForm.getTypeOfTravelDoc().getFormFieldName()).setValue("Yes").setBorderWidth(0);
            setCharList("presentTravelDocNo", passportForm.getPresentTravelDocNo(), form, DEFAULT_FONT_SIZE);
            setCharList("nmrpNo", passportForm.getNmrpNo(), form, DEFAULT_FONT_SIZE);
            setCharList("nicNo", passportForm.getNicNo(), form, DEFAULT_FONT_SIZE);
            setCharList("surName", passportForm.getSurName(), form, DEFAULT_FONT_SIZE);
            setCharList("otherNames", passportForm.getOtherNames(), form, DEFAULT_FONT_SIZE);
            setCharList("permenentAddress", passportForm.getPermenentAddress(), form, DEFAULT_FONT_SIZE);
            setCharList("district", passportForm.getDistrict(), form, DEFAULT_FONT_SIZE);
            setCharList("dob.date", passportForm.getDateOfBirth().getDate(), form, DEFAULT_FONT_SIZE);
            setCharList("dob.month", passportForm.getDateOfBirth().getMonth(), form, DEFAULT_FONT_SIZE);
            setCharList("dob.year", passportForm.getDateOfBirth().getYear(), form, DEFAULT_FONT_SIZE);
            setCharList("birthCertificate.no", passportForm.getBirthCertificate().getNo(), form, DEFAULT_FONT_SIZE);
            setCharList("birthCertificate.district", passportForm.getBirthCertificate().getDistrict(), form, DEFAULT_FONT_SIZE);
            setCharList("placeOfBirth", passportForm.getPlaceOfBirth(), form, DEFAULT_FONT_SIZE);
            form.getField(passportForm.getSex().getFormFieldName()).setValue("Yes");
            setCharList("job", passportForm.getJob(), form, DEFAULT_FONT_SIZE);
            form.getField(passportForm.getDualCitizenship().getFormFieldName()).setValue("Yes");
            setCharList("dualCitizenshipNo", passportForm.getDualCitizenshipNo(), form, DEFAULT_FONT_SIZE);
            setCharList("mobileNo", passportForm.getMobileNo(), form, DEFAULT_FONT_SIZE);
            setCharList("email", passportForm.getEmail(), form, DEFAULT_FONT_SIZE);
            setString("foreignNationality", passportForm.getForeignNationality(), form, DEFAULT_FONT_SIZE);
            setString("foreignPassportNo", passportForm.getForeignPassportNo(), form, DEFAULT_FONT_SIZE);
            setCharList("fatherNicNo", passportForm.getFatherNicNo(), form, DEFAULT_FONT_SIZE);
            setCharList("motherNicNo", passportForm.getMotherNicNo(), form, DEFAULT_FONT_SIZE);
            form.flattenFields();
        }
    }


}
