package dbbwproject.serviceunit.pdfhandler.passport;

import dbbwproject.serviceunit.pdfhandler.Validate;
import lombok.Data;

@Data
public class DateOfBirth implements Validate {
    private String date;
    private final int DATE_LENGTH = 2;
    private String month;
    private final int MONTH_LENGTH = 2;
    private String year;
    private final int YEAR_LENGTH = 4;

    @Override
    public void validate() {
        validateField(date, DATE_LENGTH, "date");
        validateField(month, MONTH_LENGTH, "month");
        validateField(year, YEAR_LENGTH, "year");
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
