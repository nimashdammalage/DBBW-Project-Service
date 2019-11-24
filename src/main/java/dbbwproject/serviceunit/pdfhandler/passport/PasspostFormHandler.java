package dbbwproject.serviceunit.pdfhandler.passport;


import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class PasspostFormHandler {

    public static void savePassportForm(PassportForm passportForm, String fileLocaion) throws IOException {
        passportForm.validate();
        File file = ResourceUtils.getFile("classpath:pasportApplication_halfEdit-Copy.pdf");
        try(PdfDocument pdf = new PdfDocument(new PdfReader(new FileInputStream(file)),
                new PdfWriter(new FileOutputStream(fileLocaion)))) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);

            form.getField(passportForm.getTypeOfService().getFormFieldName()).setValue("Yes");
            form.getField(passportForm.getTypeOfTravelDoc().getFormFieldName()).setValue("Yes");
            setCharList("presentTravelDocNo", passportForm.getPresentTravelDocNo(), form);
            setCharList("nmrpNo", passportForm.getNmrpNo(), form);
            setCharList("nicNo", passportForm.getNicNo(), form);
            setCharList("surName", passportForm.getSurName(), form);
            setCharList("otherNames", passportForm.getOtherNames(), form);
            setCharList("permenentAddress", passportForm.getPermenentAddress(), form);
            setCharList("district", passportForm.getDistrict(), form);
            setCharList("dob.date", passportForm.getDateOfBirth().getDate(), form);
            setCharList("dob.month", passportForm.getDateOfBirth().getMonth(), form);
            setCharList("dob.year", passportForm.getDateOfBirth().getYear(), form);
            setCharList("birthCertificate.no", passportForm.getBirthCertificate().getNo(), form);
            setCharList("birthCertificate.district", passportForm.getBirthCertificate().getDistrict(), form);
            setCharList("placeOfBirth", passportForm.getPlaceOfBirth(), form);
            form.getField(passportForm.getSex().getFormFieldName()).setValue("Yes");
            setCharList("job", passportForm.getJob(), form);
            form.getField(passportForm.getDualCitizenship().getFormFieldName()).setValue("Yes");
            setCharList("dualCitizenshipNo", passportForm.getDualCitizenshipNo(), form);
            setCharList("mobileNo", passportForm.getMobileNo(), form);
            setCharList("email", passportForm.getEmail(), form);
            setString("foreignNationality", passportForm.getForeignNationality(), form);
            setString("foreignPassportNo", passportForm.getForeignPassportNo(), form);
            setCharList("fatherNicNo", passportForm.getFatherNicNo(), form);
            setCharList("motherNicNo", passportForm.getMotherNicNo(), form);
            form.flattenFields();
        }
    }

    private static void setCharList(String parentField, String passportFormValue, PdfAcroForm form) {
        for (int i = 0; i < passportFormValue.length(); i++) {
            String fieldName = parentField + "." + i;
            form.getField(fieldName).setValue(Character.toString(passportFormValue.charAt(i)));
        }
    }

    private static void setString(String parentField, String passportFormValue, PdfAcroForm form) {
            form.getField(parentField).setValue(passportFormValue);
    }
}
