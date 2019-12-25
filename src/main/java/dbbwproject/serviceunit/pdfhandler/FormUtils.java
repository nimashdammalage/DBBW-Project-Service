package dbbwproject.serviceunit.pdfhandler;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;


public class FormUtils {
    public static void setCharList(String parentField, String passportFormValue, PdfAcroForm form, int DEFAULT_FONT_SIZE) {
        for (int i = 0; i < passportFormValue.length(); i++) {
            String fieldName = parentField + "." + i;
            form.getField(fieldName).setValue(Character.toString(passportFormValue.charAt(i)).toUpperCase())
                    .setFontSize(DEFAULT_FONT_SIZE)
                    .setBorderWidth(0);
        }
    }

    public static void setString(String parentField, String passportFormValue, PdfAcroForm form, int DEFAULT_FONT_SIZE) {
        form.getField(parentField).setValue(passportFormValue.toUpperCase())
                .setFontSize(DEFAULT_FONT_SIZE)
                .setBorderWidth(0);
    }

    public static void setBooleanDefualt(String parentField, PdfAcroForm form) {
        form.getField(parentField)
                .setValue("Yes")
                .setCheckType(PdfFormField.TYPE_CHECK)
                .setBorderWidth(0);
    }

    public static void setBooleanChkOrCross(String parentField, boolean value, PdfAcroForm form) {
        if (value) {
            form.getField(parentField)
                    .setValue("Yes")
                    .setCheckType(PdfFormField.TYPE_CHECK)
                    .setBorderWidth(0);
        } else {
            form.getField(parentField)
                    .setValue("Yes")
                    .setCheckType(PdfFormField.TYPE_CROSS)
                    .setBorderWidth(0);
        }

    }
}
