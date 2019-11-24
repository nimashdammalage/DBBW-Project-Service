package dbbwproject.serviceunit.pdfhandler;

import lombok.Data;

@Data
public class BirthCertificate implements Validate {
    private String no;
    private final int NO_LENGTH = 4;
    private String district;
    private final int DISTRICT_LENGTH = 12;

    @Override
    public void validate() {
        validateField(no, NO_LENGTH, "no");
        validateField(district, DISTRICT_LENGTH, "district");
    }

    private static void validateField(String field, int length, String fieldName) {
        if (field == null) {
            throw new IllegalStateException(fieldName + "can not be null in PassportForm object.");
        }
        if (field.length() > length) {
            throw new IllegalStateException(fieldName + "size must be less than or equal to " + length + " .");
        }
    }
}
